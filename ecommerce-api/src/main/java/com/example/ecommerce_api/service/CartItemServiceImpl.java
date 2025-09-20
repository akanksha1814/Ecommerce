package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.CartItem;
import com.example.ecommerce_api.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public Optional<CartItem> findByCartAndProduct(Long cartId, Long productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }
}
