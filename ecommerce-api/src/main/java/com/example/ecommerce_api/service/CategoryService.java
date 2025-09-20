package com.example.ecommerce_api.service;

import com.example.ecommerce_api.DTO.CategoryDTO;
import com.example.ecommerce_api.entity.Category;
import com.example.ecommerce_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    Optional<CategoryDTO> getCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    Optional<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO);

    boolean deleteCategory(Long id);
}