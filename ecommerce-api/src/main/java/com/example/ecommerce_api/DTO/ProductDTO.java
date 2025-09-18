package com.example.ecommerce_api.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;

    // getters and setters

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private Double price;
        private Integer stock;
        private Long categoryId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public ProductDTO build() {
            ProductDTO dto = new ProductDTO();
            dto.setId(id);
            dto.setName(name);
            dto.setDescription(description);
            dto.setPrice(price);
            dto.setStock(stock);
            dto.setCategoryId(categoryId);
            return dto;
        }
    }
}