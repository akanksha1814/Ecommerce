package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.*;
import com.example.ecommerce_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public Optional<Cart> getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    @Override
    public Cart addItemToCart(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + customerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id " + productId));

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> createCartForCustomer(customerId));

        // 🔑 Logic for adding items (simplified, assumes Cart has method addProduct)
        cart.addProduct(product, quantity);

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customer " + customerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id " + productId));

        cart.removeProduct(product);

        return cartRepository.save(cart);
    }

    @Override
    public boolean clearCart(Long customerId) {
        Optional<Cart> cartOpt = cartRepository.findByCustomerId(customerId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.clear();
            cartRepository.save(cart);
            return true;
        }
        return false;
    }

    @Override
    public Cart createCartForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + customerId));

        Cart cart = new Cart();
        cart.setCustomer(customer);
        return cartRepository.save(cart);
    }
    @Override
    public Optional<Cart> findById(Long cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}
