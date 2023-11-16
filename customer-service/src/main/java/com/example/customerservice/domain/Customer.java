package com.example.customerservice.domain;

import com.example.common.Money;
import com.example.customerservice.exception.CustomerCreditLimitExceededException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Entity
@Table(name="Customer")
@Access(AccessType.FIELD)
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Money creditLimit;

    @ElementCollection
    private Map<Long, Money> creditReservations;

    @Version
    private Long version;

    public Customer() {
    }

    public Customer(String name, Money creditLimit) {
        this.name = name;
        this.creditLimit = creditLimit;
        this.creditReservations = Collections.emptyMap();
    }

    Money availableCredit() {
        return creditLimit.subtract(
                creditReservations
                        .values()
                        .stream()
                        .reduce(Money.ZERO, Money::add)
        );
    }

    public void reserveCredit(Long orderId, Money orderTotal) {
        if (availableCredit().isGreaterThanOrEqual(orderTotal)) {
            creditReservations.put(orderId, orderTotal);
        } else {
            throw new CustomerCreditLimitExceededException();
        }
    }
}
