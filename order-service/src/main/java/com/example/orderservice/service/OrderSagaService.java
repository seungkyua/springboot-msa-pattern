package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.messaging.CreateOrderSagaData;
import com.example.orderservice.messaging.OrderDetails;
import com.example.orderservice.repository.OrderRepository;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderSagaService {

    private OrderRepository orderRepository;
    private SagaInstanceFactory sagaInstanceFactory;
    private CreateOrderSaga createOrderSaga;

    public OrderSagaService(OrderRepository orderRepository,
                            SagaInstanceFactory sagaInstanceFactory,
                            CreateOrderSaga createOrderSaga) {

        this.orderRepository = orderRepository;
        this.sagaInstanceFactory = sagaInstanceFactory;
        this.createOrderSaga = createOrderSaga;
    }

    @Transactional
    public Order createOrder(OrderDetails orderDetails) {
        CreateOrderSagaData data = new CreateOrderSagaData(orderDetails);
        sagaInstanceFactory.create(createOrderSaga, data);
        return orderRepository.findById(data.getOrderId()).get();
    }
}
