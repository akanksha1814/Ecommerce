package com.example.ecommerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One cart belongs to one customer
    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    // A cart has multiple items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    public void addProduct(Product product, int quantity) {
        // check if product already exists in cart
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                item.setQuantity(item.getQuantity() + quantity); // update qty
                return;
            }
        }
        // if new product, add new CartItem
        CartItem newItem = new CartItem(this, product, quantity);
        items.add(newItem);
    }

    public void removeProduct(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
    }

    public void clear() {
        items.clear();
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }
}
