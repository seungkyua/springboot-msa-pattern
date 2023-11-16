package com.example.orderservice.web;

import com.example.orderservice.domain.Order;
import com.example.orderservice.messaging.OrderDetails;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderSagaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private OrderSagaService orderSagaService;
    private OrderRepository orderRepository;

    public OrderController(OrderSagaService orderSagaService,
                           OrderRepository orderRepository) {
        this.orderSagaService = orderSagaService;
        this.orderRepository = orderRepository;
    }

    @PostMapping(value = "/orders")
    public CreateOrderResponse createOrder(
            @RequestBody CreateOrderRequest createOrderRequest) {

        Order order = orderSagaService.createOrder(
                new OrderDetails(createOrderRequest.getCustomerId(),
                        createOrderRequest.getOrderTotal()));

        return new CreateOrderResponse(order.getId());
    }

    @GetMapping(value = "/orders/{orderId}")
    public ResponseEntity<GetOrderResponse> getOrder(
            @PathVariable Long orderId) {

        return orderRepository
                .findById(orderId)
                .map(o -> new ResponseEntity<>(
                                new GetOrderResponse(
                                        o.getId(),
                                        o.getState(),
                                        o.getRejectionReason()),
                                HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/orders/customer/{customerId}")
    public ResponseEntity<List<GetOrderResponse>> getOrdersByCustomerId(
            @PathVariable Long customerId) {

        return new ResponseEntity<List<GetOrderResponse>>(
                orderRepository
                        .findAllByOrderDetailsCustomerId(customerId)
                        .stream()
                        .map(o -> new GetOrderResponse(
                                o.getId(), o.getState(), o.getRejectionReason()))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping(path="/healthz")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
