package com.example.common.web;

import com.example.common.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCustomerResponse {

    private Long customerId;
    private String name;
    private Money creditLimit;

    public GetCustomerResponse() {
    }

    public GetCustomerResponse(Long customerId, String name, Money creditLimit) {
        this.customerId = customerId;
        this.name = name;
        this.creditLimit = creditLimit;
    }
}
