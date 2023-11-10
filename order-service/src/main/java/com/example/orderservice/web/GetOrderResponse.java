package com.example.orderservice.web;

import com.example.orderservice.messaging.OrderState;
import com.example.orderservice.messaging.RejectionReason;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderResponse {

    private Long orderId;
    private OrderState orderState;
    private RejectionReason rejectionReason;

    public GetOrderResponse() {
    }

    public GetOrderResponse(Long orderId,
                            OrderState orderState,
                            RejectionReason rejectionReason) {
        this.orderId = orderId;
        this.orderState = orderState;
        this.rejectionReason = rejectionReason;
    }
}
