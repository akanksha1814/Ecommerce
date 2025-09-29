package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Category;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.CategoryRepository;
import com.example.ecommerce_api.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        List<Product> productsToDelete = new ArrayList<>(category.getProducts());

        for (Product product : productsToDelete) {
            for (Order order : new ArrayList<>(product.getOrders())) {

                String status = order.getStatus();
                if (status != null && !status.equals("PLACED") && !status.equals("DELIVERED")) {
                    order.setTotalPrice(order.getTotalPrice() - product.getPrice());
                    order.getProducts().remove(product);
                }
            }
        }
        categoryRepository.delete(category);
        return category;
    }
}
