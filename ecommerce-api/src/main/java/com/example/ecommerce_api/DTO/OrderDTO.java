package com.example.ecommerce_api.DTO;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalPrice;
    private String status;
    private Long customerId;
    private List<OrderItemDTO> items;
}
