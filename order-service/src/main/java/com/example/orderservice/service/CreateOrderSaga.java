package com.example.orderservice.service;

import com.example.common.Money;
import com.example.common.messaging.commands.ReserveCreditCommand;
import com.example.common.messaging.replies.CustomerCreditLimitExceeded;
import com.example.common.messaging.replies.CustomerNotFound;
import com.example.orderservice.domain.Order;
import com.example.orderservice.messaging.CreateOrderSagaData;
import com.example.orderservice.messaging.RejectionReason;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {

    private final Logger _logger = LoggerFactory.getLogger(getClass());
    private final OrderService orderService;

    public CreateOrderSaga(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public SagaDefinition<CreateOrderSagaData> getSagaDefinition() {

        return step()
                 .invokeLocal(this::create)
                 .withCompensation(this::reject)
               .step()
                 .invokeParticipant(this::reserveCredit)
                 .onReply(CustomerNotFound.class, this::handleCustomerNotFound)
                 .onReply(CustomerCreditLimitExceeded.class, this::handleCustomerCreditLimitExceeded)
               .step()
                 .invokeLocal(this::approve)
               .build();
    }

    private void handleCustomerNotFound(CreateOrderSagaData data, CustomerNotFound reply) {
        data.setRejectionReason(RejectionReason.UNKNOWN_CUSTOMER);
    }

    private void handleCustomerCreditLimitExceeded(CreateOrderSagaData data,
                                                   CustomerCreditLimitExceeded reploy) {
        data.setRejectionReason(RejectionReason.INSUFFICIENT_CREDIT);
    }

    private void create(CreateOrderSagaData data) {
        Order order = orderService.createOrder(data.getOrderDetails());
        data.setOrderId(order.getId());
    }

    private void reject(CreateOrderSagaData data) {
        orderService.rejectOrder(data.getOrderId(), data.getRejectionReason());
    }

    private CommandWithDestination reserveCredit(CreateOrderSagaData data) {
        _logger.debug("================ before sending message");
        Long orderId = data.getOrderId();
        Long customerId = data.getOrderDetails().getCustomerId();
        Money orderTotal = data.getOrderDetails().getOrderTotal();
        CommandWithDestination cwd = send(new ReserveCreditCommand(customerId, orderId, orderTotal))
                .to("customerService")
                .build();
        _logger.debug("================ after sending message");
        return cwd;
    }

    private void approve(CreateOrderSagaData data) {
        orderService.approveOrder(data.getOrderId());
    }
}
