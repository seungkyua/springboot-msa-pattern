package com.example.orderservice;

import com.example.orderservice.service.CreateOrderSaga;
import com.example.orderservice.service.OrderService;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(OptimisticLockingDecoratorConfiguration.class)
public class OrderConfiguration {

    @Bean
    public CreateOrderSaga createOrderSaga(OrderService orderService) {
        return new CreateOrderSaga(orderService);
    }
}
