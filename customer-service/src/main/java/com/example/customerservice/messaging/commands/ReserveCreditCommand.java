package com.example.customerservice.messaging.commands;

import com.example.customerservice.common.Money;
import io.eventuate.tram.commands.common.Command;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveCreditCommand  implements Command {

    private Long orderId;
    private Money orderTotal;
    private long customerId;

    public ReserveCreditCommand() {
    }

    public ReserveCreditCommand(Long customerId, Long orderId, Money orderTotal) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.orderTotal = orderTotal;
    }



}
