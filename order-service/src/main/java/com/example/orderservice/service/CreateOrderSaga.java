package com.example.orderservice.service;

import com.example.customerservice.common.Money;
import com.example.customerservice.messaging.commands.ReserveCreditCommand;
import com.example.customerservice.messaging.replies.CustomerCreditLimitExceeded;
import com.example.customerservice.messaging.replies.CustomerNotFound;
import com.example.orderservice.domain.Order;
import com.example.orderservice.messaging.CreateOrderSagaData;
import com.example.orderservice.messaging.RejectionReason;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {

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
        Long orderId = data.getOrderId();
        Long customerId = data.getOrderDetails().getCustomerId();
        Money orderTotal = data.getOrderDetails().getOrderTotal();
        return send(new ReserveCreditCommand(customerId, orderId, orderTotal))
                .to("customerService")
                .build();
    }

    private void approve(CreateOrderSagaData data) {
        orderService.approveOrder(data.getOrderId());
    }
}
