package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Category;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.CategoryRepository;
import com.example.ecommerce_api.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService; // 1. Inject ProductService

    // Using @Lazy to resolve a potential circular dependency if ProductService also needs CategoryService
    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
    ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
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
        // 2. Find the category or throw an exception.
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // 3. Create a copy of the product list to avoid modification issues while iterating.
        List<Product> productsToDelete = new ArrayList<>(category.getProducts());

        // 4. For each product in the category, call the existing product deletion logic.
        // This automatically handles all order updates and product cleanup.
        for (Product product : productsToDelete) {
            productService.deleteProduct(product.getId());
        }

        // 5. Finally, delete the now-empty category.
        categoryRepository.delete(category);
        return category;
    }
}
