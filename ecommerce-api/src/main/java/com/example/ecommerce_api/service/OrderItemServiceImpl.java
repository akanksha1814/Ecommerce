package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.OrderItem;
import com.example.ecommerce_api.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void delete(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public List<OrderItem> getAllItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public Optional<OrderItem> findByOrderAndProduct(Long orderId, Long productId) {
         List<OrderItem> orderItems =orderItemRepository.findByOrder_IdAndProduct_Id(orderId, productId);
         return orderItems.isEmpty() ? Optional.empty() : Optional.of(orderItems.get(0));
    }
}
