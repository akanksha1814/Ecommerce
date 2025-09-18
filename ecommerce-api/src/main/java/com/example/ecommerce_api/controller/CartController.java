package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.entity.Cart;
import com.example.ecommerce_api.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get cart by customer
    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomerId(customerId));
    }

    // Add item to cart
    @PostMapping("/{customerId}/add/{productId}")
    public ResponseEntity<Cart> addItem(@PathVariable Long customerId,
                                        @PathVariable Long productId,
                                        @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(customerId, productId, quantity));
    }

    // Remove item from cart
    @DeleteMapping("/{customerId}/remove/{productId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long customerId,
                                           @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(customerId, productId));
    }

    // Clear cart
    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
