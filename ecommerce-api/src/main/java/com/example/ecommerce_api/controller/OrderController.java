package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.dto.GenericResponse;
import com.example.ecommerce_api.dto.OrderDTO;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.mapper.OrderMapper;
import com.example.ecommerce_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @Operation(summary = "Create a new order for a customer and return the new order ID")
    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<String> createOrder(@PathVariable Long customerId) {
        Order createdOrder = orderService.createOrder(customerId);
        return new ResponseEntity<>("Order with id:"+createdOrder.getId()+" is created", HttpStatus.CREATED);
    }

    @Operation(summary = "Get an order by its ID")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        Order orderEntity = orderService.getOrderById(orderId);

        // 3. Map the entity to a DTO for a clean, non-recursive response
        OrderDTO orderDTO = orderMapper.toOrderDTO(orderEntity);

        // 4. Return the DTO
        return ResponseEntity.ok(orderDTO);
    }

    @Operation(summary = "Get all orders for a specific customer")
    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orderEntities = orderService.getOrdersByCustomerId(customerId);
        List<OrderDTO> orderDTOs = orderEntities.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }
    @Operation(summary = "Get a specific order for a specific customer")
    @GetMapping("/customers/{customerId}/orders/{orderId}")
    public ResponseEntity<OrderDTO> getCustomerOrder(
            @PathVariable Long customerId,
            @PathVariable Long orderId) {

        Order orderEntity = orderService.getCustomerOrderById(customerId, orderId);
        OrderDTO orderDTO = orderMapper.toOrderDTO(orderEntity);

        return ResponseEntity.ok(orderDTO);
    }
    @Operation(summary = "Delete a specific order for a customer")
    @DeleteMapping("/customers/{customerId}/orders/{orderId}")
    public ResponseEntity<GenericResponse> deleteCustomerOrder(@PathVariable Long customerId, @PathVariable Long orderId) {
        orderService.deleteCustomerOrder(customerId, orderId);
        return ResponseEntity.ok(new GenericResponse("Order with ID " + orderId + " deleted successfully."));
    }

    @Operation(summary = "Delete all orders for a customer")
    @DeleteMapping("/customers/{customerId}/orders")
    public ResponseEntity<GenericResponse> deleteAllOrdersByCustomer(@PathVariable Long customerId) {
        orderService.deleteAllOrdersByCustomerId(customerId);
        return ResponseEntity.ok(new GenericResponse("All orders for customer with ID " + customerId + " deleted successfully."));
    }

    // Inside OrderController.java

    @Operation(summary = "Remove a product from an order")
    @DeleteMapping("/orders/{orderId}/products/{productId}")
    public ResponseEntity<OrderDTO> removeProductFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long productId) {
        Order updatedOrder = orderService.removeProductFromOrder( orderId, productId);
        return ResponseEntity.ok(orderMapper.toOrderDTO(updatedOrder));
    }

    @Operation(summary = "Change order status to PLACED")
    @PostMapping("/customers/{customerId}/orders/{orderId}/place")
    public ResponseEntity<OrderDTO> placeOrder(
            @PathVariable Long customerId,
            @PathVariable Long orderId) {
        Order placedOrder = orderService.placeOrder(customerId, orderId);
        return ResponseEntity.ok(orderMapper.toOrderDTO(placedOrder));
    }

    @Operation(summary = "Change order status to DELIVERED")
    @PostMapping("/orders/{orderId}/deliver")
    public ResponseEntity<OrderDTO> deliverOrder(@PathVariable Long orderId) {
        Order deliveredOrder = orderService.deliverOrder(orderId);
        return ResponseEntity.ok(orderMapper.toOrderDTO(deliveredOrder));
    }
}