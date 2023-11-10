package com.example.orderservice.web;

import com.example.customerservice.common.Money;
import lombok.Getter;

@Getter
public class CreateOrderRequest {
    private Money orderTotal;
    private Long customerId;

    public CreateOrderRequest() {
    }
    public CreateOrderRequest(Long customerId, Money orderTotal) {
        this.customerId = customerId;
        this.orderTotal = orderTotal;
    }
}
