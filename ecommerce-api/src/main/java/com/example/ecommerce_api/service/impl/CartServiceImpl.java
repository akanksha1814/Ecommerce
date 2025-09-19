package com.example.ecommerce_api.service.impl;

import com.example.ecommerce_api.entity.*;
import com.example.ecommerce_api.repository.*;
import com.example.ecommerce_api.service.CartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart getCartByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().customer(customer).build();
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public Cart addItemToCart(Long customerId, Long productId, int quantity) {
        Cart cart = getCartByCustomerId(customerId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // check if item already exists
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long customerId, Long productId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
