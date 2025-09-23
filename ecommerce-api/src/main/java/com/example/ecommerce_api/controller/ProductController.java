package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.ApiResponse;
import com.example.ecommerce_api.dto.ProductDto;
import com.example.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Add a new product to the inventory")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody Object request) {
        try {
            ProductDto productDto = productService.createProduct(request);
            ApiResponse<ProductDto> response = ApiResponse.<ProductDto>builder()
                    .success(true)
                    .message("Product created successfully")
                    .data(productDto)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<ProductDto> response = ApiResponse.<ProductDto>builder()
                    .success(false)
                    .message("Failed to create product")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        try {
            List<ProductDto> products = productService.getAllProducts();
            ApiResponse<List<ProductDto>> response = ApiResponse.<List<ProductDto>>builder()
                    .success(true)
                    .message("Products retrieved successfully")
                    .data(products)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ProductDto>> response = ApiResponse.<List<ProductDto>>builder()
                    .success(false)
                    .message("Failed to retrieve products")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product found successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        try {
            ProductDto product = productService.getProductById(id);
            ApiResponse<ProductDto> response = ApiResponse.<ProductDto>builder()
                    .success(true)
                    .message("Product found successfully")
                    .data(product)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ProductDto> response = ApiResponse.<ProductDto>builder()
                    .success(false)
                    .message("Product not found")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update product", description = "Update an existing product's information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody Object request) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, request);
            ApiResponse<ProductDto> response = ApiResponse.<ProductDto>builder()
                    .success(true)
                    .message("Product updated successfully")
                    .data(updatedProduct)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ProductDto> response = ApiResponse.<ProductDto>builder()
                    .success(false)
                    .message("Failed to update product")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Product deleted successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message("Failed to delete product")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Get products by category", description = "Retrieve all products in a specific category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(
            @Parameter(description = "Product category") @PathVariable String category) {
        try {
            List<ProductDto> products = productService.getProductsByCategory(category);
            ApiResponse<List<ProductDto>> response = ApiResponse.<List<ProductDto>>builder()
                    .success(true)
                    .message("Products retrieved successfully for category: " + category)
                    .data(products)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ProductDto>> response = ApiResponse.<List<ProductDto>>builder()
                    .success(false)
                    .message("Failed to retrieve products for category")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
