package com.example.customerservice.service;

import com.example.customerservice.exception.CustomerCreditLimitExceededException;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.common.messaging.commands.ReserveCreditCommand;
import com.example.common.messaging.replies.CustomerCreditLimitExceeded;
import com.example.common.messaging.replies.CustomerCreditReserved;
import com.example.common.messaging.replies.CustomerNotFound;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

public class CustomerCommandHandler {

    private final Logger _logger = LoggerFactory.getLogger(getClass());
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
        _logger.debug("-------------------- received message");
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
