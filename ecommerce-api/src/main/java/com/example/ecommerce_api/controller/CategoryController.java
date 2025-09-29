package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.CategoryDTO;
import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.entity.Category;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.mapper.CategoryMapper;
import com.example.ecommerce_api.mapper.OrderMapper;
import com.example.ecommerce_api.service.CategoryService;
import com.example.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryMapper categoryMapper;
    private final OrderMapper orderMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper,ProductService productService,OrderMapper orderMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.productService=productService;
        this.orderMapper=orderMapper;
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
    @Operation(summary = "Get all products for a specific category")
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        // 3. Call the new service method to get the entities
        List<Product> productEntities = productService.getProductsByCategoryId(categoryId);

        // 4. Map the entities to DTOs for a clean response
        List<ProductDTO> productDTOs = productEntities.stream()
                .map(orderMapper::toProductDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDTOs);
    }

    @Operation(summary = "Get a category by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryMapper.toCategoryDTO(category));
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category categoryToCreate = categoryMapper.toCategoryEntity(categoryDTO);
        Category createdCategory = categoryService.createCategory(categoryToCreate);
        return new ResponseEntity<>(categoryMapper.toCategoryDTO(createdCategory), HttpStatus.CREATED);
    }

//    @Operation(summary = "Delete a category by its ID")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
//        categoryService.deleteCategory(id);
//        return ResponseEntity.noContent().build();
//    }
    @Operation(summary = "Delete a category by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        // 1. The service now returns the deleted category entity.
        Category deletedCategory = categoryService.deleteCategory(id);

        // 2. Map the entity to a DTO for a clean response.
        CategoryDTO responseDTO = categoryMapper.toCategoryDTO(deletedCategory);

        // 3. Return 200 OK with the deleted category's details in the body.
        return ResponseEntity.ok(responseDTO);
    }
}