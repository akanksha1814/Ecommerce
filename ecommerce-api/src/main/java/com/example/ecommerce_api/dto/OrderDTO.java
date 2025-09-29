package com.example.ecommerce_api.dto;

import java.util.List;
import java.util.Objects;

public class OrderDTO {

    private Long id;
    private Double totalPrice;
    private String status;
    private Long customerId;
    private List<OrderItemDTO> items;

    // 1. No-Argument Constructor
    public OrderDTO() {
    }

    // 2. All-Argument Constructor
    public OrderDTO(Long id, Double totalPrice, String status, Long customerId, List<OrderItemDTO> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.customerId = customerId;
        this.items = items;
    }

    // 3. Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    // 4. (Optional but recommended) equals(), hashCode(), and toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        int itemCount = (items != null) ? items.size() : 0;
        return "OrderDTO{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                ", items=" + itemCount +
                '}';
    }
}