package com.example.ecommerce_api.dto;

public class ProductUpdateDTO {
    private String name;
    private String description;
    private Double price;

    // Constructors
    public ProductUpdateDTO() {}

    public ProductUpdateDTO(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
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
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}