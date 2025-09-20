package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Cart;

import java.util.Optional;

public interface CartService {
    Optional<Cart> getCartByCustomerId(Long customerId);
    Cart addItemToCart(Long customerId, Long productId, int quantity);
    Cart removeItemFromCart(Long customerId, Long productId);
    boolean clearCart(Long customerId);
    Cart createCartForCustomer(Long customerId);

    Optional<Cart> findById(Long cartId);
    Cart save(Cart cart);
}
