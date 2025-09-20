package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order saveOrder(Order order);
    void deleteOrder(Order order);
    Optional<Order> getOrderById(Long id);
    List<Order> getAllOrders();
    List<Order> getOrdersByCustomer(Long customerId);
}
