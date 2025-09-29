package com.example.ecommerce_api.mapper;

import com.example.ecommerce_api.dto.OrderDTO;
import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.Set;

@Component
public class OrderMapper {
    private final OrderItemMapper orderItemMapper; // 1. Inject the new mapper

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setCustomerId(order.getCustomer().getId());

        // 2. Update this logic to map OrderItems to OrderItemDTOs
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(orderItemMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }




    public ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock()); // Map stock
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId()); // Map categoryId
        }
        return dto;
    }
}