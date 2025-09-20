package com.example.ecommerce_api.repository;

import com.example.ecommerce_api.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {



    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
}
