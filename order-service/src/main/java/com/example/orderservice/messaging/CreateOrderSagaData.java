package com.example.orderservice.messaging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderSagaData {
    private OrderDetails orderDetails;
    private Long orderId;
    private RejectionReason rejectionReason;

    public CreateOrderSagaData() {
    }

    public CreateOrderSagaData(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
