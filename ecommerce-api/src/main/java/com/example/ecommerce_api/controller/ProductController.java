package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.GenericResponse;
import com.example.ecommerce_api.dto.OrderDTO;
import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.dto.ProductUpdateDTO;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.mapper.OrderMapper;
import com.example.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Product Management", description = "APIs for managing products and their relation to orders")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Autowired
    private OrderMapper orderMapper;

    @Operation(summary = "Create a new product")
    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>("Product with id:"+createdProduct.getId()+"is created", HttpStatus.CREATED);
    }

    @Operation(summary = "Get all products")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Partially update a product's details and adjust order totals")
    @PatchMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO updateDTO) {

        Product updatedProductEntity = productService.updateProduct(id, updateDTO);

        ProductDTO responseDTO = orderMapper.toProductDTO(updatedProductEntity);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Delete a product and remove it from all associated orders")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<GenericResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new GenericResponse("Product with ID " + id + " deleted and all orders updated successfully."));
    }

    @Operation(summary = "Add a product to an existing order")
    @PostMapping("/orders/{orderId}/products/{productId}")
    public ResponseEntity<OrderDTO> addProductToOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        Order updatedOrder = productService.addProductToOrder(orderId, productId);
        OrderDTO responseDTO = orderMapper.toOrderDTO(updatedOrder);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Get all products within a specific order")
    @GetMapping("/orders/{orderId}/products")
    public ResponseEntity<Set<ProductDTO>> getProductsInOrder(@PathVariable Long orderId) {
        Set<Product> productEntities = productService.getProductsByOrderId(orderId);
        Set<ProductDTO> productDTOs = productEntities.stream()
                .map(orderMapper::toProductDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(productDTOs);
    }
}