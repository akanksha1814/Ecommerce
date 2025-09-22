package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Helper method to validate JWT token from header
    private boolean isTokenValid(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                JwtUtil.validateToken(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, HttpServletRequest request) {
        if (!isTokenValid(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token");
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<?> getAllProducts(HttpServletRequest request) {
        if (!isTokenValid(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        if (!isTokenValid(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token");
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product, HttpServletRequest request) {
        if (!isTokenValid(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token");
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
