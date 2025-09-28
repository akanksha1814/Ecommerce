package com.example.ecommerce_api.dto;

import java.util.Objects;
import java.util.Set;

public class OrderDTO {

    private Long id;
    private Double totalPrice;
    private String status;
    private Long customerId;
    private Set<ProductDTO> products;

    // 1. No-Argument Constructor
    public OrderDTO() {
    }

    // 2. All-Argument Constructor
    public OrderDTO(Long id, Double totalPrice, String status, Long customerId, Set<ProductDTO> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.customerId = customerId;
        this.products = products;
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

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
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
        return "OrderDTO{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                ", products=" + products.size() + " items" +
                '}';
    }
}