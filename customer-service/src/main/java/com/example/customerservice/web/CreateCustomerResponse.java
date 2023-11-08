package com.example.customerservice.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerResponse {

    private Long customerId;
    public CreateCustomerResponse() {
    }

    public CreateCustomerResponse(Long customerId) {
        this.customerId = customerId;
    }
}
