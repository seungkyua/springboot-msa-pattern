package com.example.orderservice.domain;

import com.example.orderservice.messaging.OrderDetails;
import com.example.orderservice.messaging.OrderState;
import com.example.orderservice.messaging.RejectionReason;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="orders")
@Access(AccessType.FIELD)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @Embedded
    private OrderDetails orderDetails;

    @Enumerated(EnumType.STRING)
    private RejectionReason rejectionReason;

    @Version
    private Long version;

    public Order() {
    }

    public Order(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
        this.state = OrderState.PENDING;
    }

    public static Order createOrder(OrderDetails orderDetails) {
        return new Order(orderDetails);
    }

    public void approve() {
        this.state = OrderState.APPROVED;
    }

    public void reject(RejectionReason rejectionReason) {
        this.state = OrderState.REJECTED;
        this.rejectionReason = rejectionReason;
    }

}
