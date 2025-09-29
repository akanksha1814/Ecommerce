package com.example.ecommerce_api.mapper;

import com.example.ecommerce_api.dto.CategoryDTO;
import com.example.ecommerce_api.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(category.getId(), category.getName());
    }

    public Category toCategoryEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }
}