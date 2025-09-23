package com.example.ecommerce_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long order_id;

    @NotNull(message = "Customer ID is required")
    private Long customer_id;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private Double totalAmount;

    @NotBlank(message = "Order status is required")
    private String orderStatus;

    private String orderDate;

    private List<OrderItemDto> orderItems;
}
