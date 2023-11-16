package com.example.orderservice.web;

import com.example.common.Money;
import com.example.orderservice.domain.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCustomerHistoryResponse {
    private Long customerId;
    private String name;
    private Money creditLimit;
    private List<Order> orders;

    public GetCustomerHistoryResponse() {
    }

    public GetCustomerHistoryResponse(Long customerId,
                                      String name,
                                      Money creditLimit,
                                      List<Order> orders) {
        this.customerId =customerId;
        this.name = name;
        this.creditLimit = creditLimit;
        this.orders = orders;
    }
}
