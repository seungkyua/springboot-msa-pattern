package com.example.orderservice.messaging;

import com.example.customerservice.common.Money;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

@Embeddable
@Getter
public class OrderDetails {

    private Long customerId;

    @Embedded
    Money orderTotal;

    public OrderDetails() {
    }

    public OrderDetails(Long customerId, Money orderTotal) {
        this.customerId = customerId;
        this.orderTotal = orderTotal;
    }
}
