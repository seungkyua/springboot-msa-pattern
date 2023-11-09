package com.example.customerservice;

import com.example.customerservice.service.CustomerCommandHandler;
import com.example.customerservice.service.CustomerService;
import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(OptimisticLockingDecoratorConfiguration.class)
public class CustomerConfiguration {

    @Bean
    public CustomerCommandHandler customerCommandHandler(CustomerService customerService) {
        return new CustomerCommandHandler(customerService);
    }

    @Bean
    public CommandDispatcher consumerCommandDispatcher(
            CustomerCommandHandler target,
            SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {

        return sagaCommandDispatcherFactory.make(
                "customerCommandDispatcher",
                target.commandHandlerDefinitions());
    }
}
