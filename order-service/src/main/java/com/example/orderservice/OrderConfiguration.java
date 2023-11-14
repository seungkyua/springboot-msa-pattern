package com.example.orderservice;

import com.example.orderservice.proxies.CustomerDestinations;
import com.example.orderservice.proxies.CustomerServiceProxy;
import com.example.orderservice.service.CreateOrderSaga;
import com.example.orderservice.service.OrderService;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Import(OptimisticLockingDecoratorConfiguration.class)
public class OrderConfiguration {

    @Bean
    public CreateOrderSaga createOrderSaga(OrderService orderService) {
        return new CreateOrderSaga(orderService);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public CustomerServiceProxy customerServiceProxy(CustomerDestinations customerDestinations,
                                                     WebClient client) {

        return new CustomerServiceProxy(client, customerDestinations.getCustomerServiceUrl());
    }
}
