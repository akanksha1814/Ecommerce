package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.*;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.OrderItem;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.mapper.OrderItemMapper;
import com.example.ecommerce_api.mapper.OrderMapper;
import com.example.ecommerce_api.service.OrderService;
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
    private final OrderService orderService;
    private final OrderItemMapper orderItemMapper;

    public ProductController(ProductService productService,OrderService orderService,OrderItemMapper orderItemMapper) {
        this.productService = productService;
        this.orderService=orderService;
        this.orderItemMapper=orderItemMapper;
    }
    @Autowired
    private OrderMapper orderMapper;

    @Operation(summary = "Create a new product within a category")
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        // The service layer will handle all the logic, including finding the category
        Product newProduct = productService.createProduct(productCreateDTO);
        ProductDTO responseDTO = orderMapper.toProductDTO(newProduct);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all products")
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {

        List<Product> productEntities = productService.getAllProducts();
        List<ProductDTO> productDTOs = productEntities.stream()
                .map(orderMapper::toProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
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
    // We'll move this to OrderController for better REST structure
    @Operation(summary = "Get all items within a specific order")
    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemDTO>> getItemsInOrder(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getItemsByOrderId(orderId);
        // You'll need an OrderItemMapper to convert List<OrderItem> to List<OrderItemDTO>
        List<OrderItemDTO> dtos = items.stream().map(orderItemMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
//    @Operation(summary = "Get all products within a specific order")
//    @GetMapping("/orders/{orderId}/products")
//    public ResponseEntity<Set<ProductDTO>> getProductsInOrder(@PathVariable Long orderId) {
//        List<Product> productEntities = productService.getProductsByOrderId(orderId);
//        Set<ProductDTO> productDTOs = productEntities.stream()
//                .map(orderMapper::toProductDTO)
//                .collect(Collectors.toSet());
//        return ResponseEntity.ok(productDTOs);
//    }
}