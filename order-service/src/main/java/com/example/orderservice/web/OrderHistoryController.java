package com.example.orderservice.web;

import com.example.common.web.GetCustomerResponse;
import com.example.orderservice.domain.Order;
import com.example.orderservice.proxies.CustomerServiceProxy;
import com.example.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderHistoryController {

    private final OrderRepository orderRepository;
    private final CustomerServiceProxy customerService;
    private final Logger _logger = LoggerFactory.getLogger(getClass());

    public OrderHistoryController(OrderRepository orderRepository,
                                  CustomerServiceProxy customerService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @GetMapping(value = "/orders/customer/{customerId}/orderhistory")
    public Mono<ResponseEntity<GetCustomerHistoryResponse>> getOrderHistory(@PathVariable Long customerId) {

        Mono<Optional<GetCustomerResponse>> customer = customerService.findCustomerById(customerId);

        List<Order> orderList = orderRepository.findAllByOrderDetailsCustomerId(customerId);
        Mono<List<Order>> orders = Mono.just(orderList);

        Mono<ResponseEntity<GetCustomerHistoryResponse>> map = Mono
                .zip(customer, orders)
                .map(possibleCustomerAndOrders ->
                            possibleCustomerAndOrders.getT1()
                            .map(c -> {
                                List<Order> os = possibleCustomerAndOrders.getT2();
                                GetCustomerHistoryResponse gr = new GetCustomerHistoryResponse(
                                        c.getCustomerId(),
                                        c.getName(),
                                        c.getCreditLimit(),
                                        os);
                                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(gr);
                            })
                            .orElseGet(() -> ResponseEntity.notFound().build())
                );

        return map;
    }







//    public Mono<ResponseEntity<GetCustomerHistoryResponse>> getOrderHistory(@PathVariable Long customerId) {
//
//        Mono<Optional<GetCustomerResponse>> customer = customerService.findCustomerById(customerId);
//
//        List<Order> orderList = orderRepository.findAllByOrderDetailsCustomerId(customerId);
//        Mono<List<Order>> orders = Mono.just(orderList);
//
//        Mono<Optional<GetCustomerHistoryResponse>> map = Mono
//                .zip(customer, orders)
//                .map(possibleCustomerAndOrders ->
//                        possibleCustomerAndOrders.getT1()
//                                .map(c -> {
//                                    List<Order> os = possibleCustomerAndOrders.getT2();
//                                    return new GetCustomerHistoryResponse(
//                                            c.getCustomerId(),
//                                            c.getName(),
//                                            c.getCreditLimit(),
//                                            os);
//                                })
//                );
//
//        return map.map(maybe ->
//                maybe.map(c ->
//                        ResponseEntity.ok()
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .body(c))
//                      .orElseGet(() -> ResponseEntity.notFound().build()));
//    }






//    public Mono<GetCustomerHistoryResponse> getOrderHistory(@PathVariable Long customerId) {
//
//        _logger.debug("customerId = {}", customerId);
//
//        Mono<Optional<GetCustomerResponse>> customer = customerService.findCustomerById(customerId);
//
//        List<Order> orderList = orderRepository.findAllByOrderDetailsCustomerId(customerId);
//        Mono<List<Order>> orders = Mono.just(orderList);
//
//        Mono<GetCustomerHistoryResponse> map = Mono
//                .zip(customer, orders)
//                .map(possibleCustomerAndOrders ->
//                        possibleCustomerAndOrders.getT1()
//                                .map(c -> {
//                                    List<Order> os = possibleCustomerAndOrders.getT2();
//                                    return new GetCustomerHistoryResponse(
//                                            c.getCustomerId(),
//                                            c.getName(),
//                                            c.getCreditLimit(),
//                                            os);
//                                })
//                                .orElseGet(GetCustomerHistoryResponse::new));
//
//        return map;
//    }




//    public Mono<Optional<GetCustomerHistoryResponse>> getOrderHistory(@PathVariable Long customerId) {
//
//        _logger.debug("customerId = {}", customerId);
//
//        Mono<Optional<GetCustomerResponse>> customer = customerService.findCustomerById(customerId);
//
//        List<Order> orderList = orderRepository.findAllByOrderDetailsCustomerId(customerId);
//        Mono<List<Order>> orders = Mono.just(orderList);
//
//        Mono<Optional<GetCustomerHistoryResponse>> map = Mono
//                .zip(customer, orders)
//                .map(possibleCustomerAndOrders ->
//                        possibleCustomerAndOrders.getT1()
//                                .map(c -> {
//                                    List<Order> os = possibleCustomerAndOrders.getT2();
//                                    return new GetCustomerHistoryResponse(
//                                            c.getCustomerId(),
//                                            c.getName(),
//                                            c.getCreditLimit(),
//                                            os);
//                                }));
//
//        return map;
//    }

}
