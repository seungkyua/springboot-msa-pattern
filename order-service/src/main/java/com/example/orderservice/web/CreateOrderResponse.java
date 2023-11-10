package com.example.orderservice.web;

import lombok.Getter;

@Getter
public class CreateOrderResponse {
    private Long orderId;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(Long orderId) {
        this.orderId = orderId;
    }
}
