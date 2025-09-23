package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.ApiResponse;
import com.example.ecommerce_api.dto.OrderDto;
import com.example.ecommerce_api.service.OrderService;
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
@RequestMapping("/api/orders")
//@RequiredArgsConstructor
@Validated
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order", description = "Place a new order with order items")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody Object request) {
        try {
            OrderDto orderDto = orderService.createOrder(request);
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(true)
                    .message("Order created successfully")
                    .data(orderDto)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(false)
                    .message("Failed to create order")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        try {
            List<OrderDto> orders = orderService.getAllOrders();
            ApiResponse<List<OrderDto>> response = ApiResponse.<List<OrderDto>>builder()
                    .success(true)
                    .message("Orders retrieved successfully")
                    .data(orders)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<OrderDto>> response = ApiResponse.<List<OrderDto>>builder()
                    .success(false)
                    .message("Failed to retrieve orders")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order found successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @Parameter(description = "Order ID") @PathVariable Long id) {
        try {
            OrderDto order = orderService.getOrderById(id);
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(true)
                    .message("Order found successfully")
                    .data(order)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(false)
                    .message("Order not found")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @Parameter(description = "Order ID") @PathVariable Long id,
            @RequestParam String status) {
        try {
            OrderDto updatedOrder = orderService.updateOrderStatus(id, status);
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(true)
                    .message("Order status updated successfully")
                    .data(updatedOrder)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(false)
                    .message("Failed to update order status")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get orders by customer", description = "Retrieve all orders for a specific customer")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer orders retrieved successfully")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        try {
            List<OrderDto> orders = orderService.getOrdersByCustomerId(customerId);
            ApiResponse<List<OrderDto>> response = ApiResponse.<List<OrderDto>>builder()
                    .success(true)
                    .message("Customer orders retrieved successfully")
                    .data(orders)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<OrderDto>> response = ApiResponse.<List<OrderDto>>builder()
                    .success(false)
                    .message("Failed to retrieve customer orders")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Delete order", description = "Delete an order by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @Parameter(description = "Order ID") @PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Order deleted successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message("Failed to delete order")
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}