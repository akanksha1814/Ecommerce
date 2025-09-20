package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.CartItem;

import java.util.Optional;

public interface CartItemService {
    CartItem save(CartItem cartItem);
    void delete(CartItem cartItem);
    Optional<CartItem> findById(Long id);
    Optional<CartItem> findByCartAndProduct(Long cartId, Long productId);
}
