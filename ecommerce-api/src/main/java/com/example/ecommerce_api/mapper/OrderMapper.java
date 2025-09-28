package com.example.ecommerce_api.mapper;

import com.example.ecommerce_api.dto.OrderDTO;
import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.Set;

@Component
public class OrderMapper {

    public OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setCustomerId(order.getCustomer().getId());

        Set<ProductDTO> productDTOs = order.getProducts().stream()
                .map(this::toProductDTO)
                .collect(Collectors.toSet());
        dto.setProducts(productDTOs);

        return dto;
    }

    public ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        return dto;
    }
}