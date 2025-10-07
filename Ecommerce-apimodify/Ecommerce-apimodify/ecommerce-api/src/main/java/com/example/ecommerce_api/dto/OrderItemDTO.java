package com.example.ecommerce_api.dto;

public class OrderItemDTO {
    private Long productId;
    private String productName;
    private double priceAtPurchase;
    private int quantity;
    // Add constructors, getters, and setters

    public OrderItemDTO(Long productId, String productName, double priceAtPurchase, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
    }

    public OrderItemDTO() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}