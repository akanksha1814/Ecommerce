package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.DTO.ProductDTO;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    // Helper: convert Entity → DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        return dto;
    }

    // Helper: convert DTO → Entity
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        if(product.getCategory() != null) {
            product.setCategory(product.getCategory());
        }
        // category assignment will be handled in service if needed
        return product;
    }

    @Operation(summary = "Create a new product", description = "Add a new product to the catalog")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product saved = productService.createProduct(convertToEntity(productDTO));
        return ResponseEntity.ok(convertToDTO(saved));
    }

    @Operation(summary = "Get product by ID", description = "Retrieve product details by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(convertToDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtos = productService.getAllProducts()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Update product", description = "Update an existing product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product updated = productService.updateProduct(id, convertToEntity(productDTO));
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get products by category", description = "Retrieve all products under a specific category")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> dtos = productService.getProductsByCategory(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
