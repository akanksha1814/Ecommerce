package com.example.ecommerce_api.mapper;

import com.example.ecommerce_api.dto.OrderItemDTO;
import com.example.ecommerce_api.entity.OrderItem;
import com.example.ecommerce_api.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDTO toDTO(OrderItem item) {
        if (item == null) {
            return null;
        }

        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductName(item.getProductName());
        dto.setPriceAtPurchase(item.getPriceAtPurchase());
        dto.setQuantity(item.getQuantity());

        // Safely get the product ID, which could be null if the product was deleted
        Product product = item.getProduct();
        if (product != null) {
            dto.setProductId(product.getId());
        }

        return dto;
    }
}