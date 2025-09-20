package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    OrderItem save(OrderItem orderItem);
    void delete(OrderItem orderItem);
    Optional<OrderItem> findById(Long id);
    List<OrderItem> getAllItems();
    Optional<OrderItem> findByOrderAndProduct(Long orderId, Long productId);
}