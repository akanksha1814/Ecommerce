package com.example.ecommerce_api.DTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private Long cartId;
    private Long customerId;
    private String customerName;
    private List<CartItemDTO> items;
    private double totalAmount;  // sum of all item totals
}
