package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Cart;

public interface CartService {
    Cart getCartByCustomerId(Long customerId);
    Cart addItemToCart(Long customerId, Long productId, int quantity);
    Cart removeItemFromCart(Long customerId, Long productId);
    void clearCart(Long customerId);
}
