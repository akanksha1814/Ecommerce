package com.example.ecommerce_api.service;



import com.example.ecommerce_api.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Object request);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto updateOrderStatus(Long id, String status);
    void deleteOrder(Long id);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getOrdersByStatus(String status);
}
