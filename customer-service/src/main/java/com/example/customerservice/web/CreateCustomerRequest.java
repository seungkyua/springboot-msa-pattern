package com.example.customerservice.web;

import com.example.customerservice.common.Money;
import lombok.Getter;

@Getter
public class CreateCustomerRequest {

    private String name;
    private Money creditLimit;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String name, Money creditLimit) {
        this.name = name;
        this.creditLimit = creditLimit;
    }
}
