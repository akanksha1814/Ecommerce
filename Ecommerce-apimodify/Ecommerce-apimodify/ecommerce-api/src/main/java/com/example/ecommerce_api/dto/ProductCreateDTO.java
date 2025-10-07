package com.example.ecommerce_api.dto;

import java.util.Objects;

public class ProductCreateDTO {

    private String name;
    private String description;
    private double price;
    private int stock;
    private Long categoryId;

    // 1. No-Argument Constructor
    public ProductCreateDTO() {
    }

    // 2. All-Argument Constructor
    public ProductCreateDTO(String name, String description, double price, int stock, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
    }

    // 3. Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // 4. (Optional but recommended) equals(), hashCode(), and toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCreateDTO that = (ProductCreateDTO) o;
        return Double.compare(that.price, price) == 0 &&
                stock == that.stock &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, stock, categoryId);
    }

    @Override
    public String toString() {
        return "ProductCreateDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryId=" + categoryId +
                '}';
    }
}