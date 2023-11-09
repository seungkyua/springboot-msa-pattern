package com.example.customerservice.service;

import com.example.customerservice.exception.CustomerCreditLimitExceededException;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.messaging.commands.ReserveCreditCommand;
import com.example.customerservice.messaging.replies.CustomerCreditLimitExceeded;
import com.example.customerservice.messaging.replies.CustomerCreditReserved;
import com.example.customerservice.messaging.replies.CustomerNotFound;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

public class CustomerCommandHandler {

    private final CustomerService customerService;

    public CustomerCommandHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("customerService")
                .onMessage(ReserveCreditCommand.class, this::reserveCredit)
                .build();
    }

    public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {
        ReserveCreditCommand cmd = cm.getCommand();
        try {
            customerService.reservedCredit(cmd.getCustomerId(), cmd.getOrderId(), cmd.getOrderTotal());
            return withSuccess(new CustomerCreditReserved());
        } catch (CustomerNotFoundException e) {
            return withFailure(new CustomerNotFound());
        } catch (CustomerCreditLimitExceededException e) {
            return withFailure(new CustomerCreditLimitExceeded());
        }
    }
}
