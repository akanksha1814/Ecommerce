package com.example.ecommerce_api.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private double totalPrice; // price * quantity
}
