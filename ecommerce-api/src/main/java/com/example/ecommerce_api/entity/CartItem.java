package com.example.ecommerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many cart items belong to one cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Each cart item refers to a product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    // -----------------------
    // 🔹 Convenience Constructor
    // -----------------------
    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}
