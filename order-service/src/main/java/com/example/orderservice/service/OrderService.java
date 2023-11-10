package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.messaging.OrderDetails;
import com.example.orderservice.messaging.RejectionReason;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderDetails orderDetails) {
        Order order = Order.createOrder(orderDetails);
        orderRepository.save(order);
        return order;
    }

    public void approveOrder(Long orderId) {
        orderRepository.findById(orderId)
                .get()
                .approve();
    }

    public void rejectOrder(Long orderId, RejectionReason rejectionReason) {
        orderRepository.findById(orderId)
                .get()
                .reject(rejectionReason);
    }
}
