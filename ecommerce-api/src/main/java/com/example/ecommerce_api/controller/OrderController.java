package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.DTO.OrderDTO;
import com.example.ecommerce_api.DTO.OrderItemDTO;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.OrderItem;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.service.CustomerService;
import com.example.ecommerce_api.service.OrderItemService;
import com.example.ecommerce_api.service.OrderService;
import com.example.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "APIs for managing orders and order items")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService,
                           OrderItemService orderItemService,
                           ProductService productService,
                           CustomerService customerService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.customerService = customerService;
    }

    // === HELPER METHOD TO MAP ENTITY → DTO ===
    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            return itemDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    // === ORDER ENDPOINTS ===
    @Operation(summary = "Get order by ID", description = "Retrieve order details including items and customer info")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok(mapToDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all orders", description = "Retrieve all orders with their items and customer details")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Create a new order", description = "Create an order for a specific customer with order items")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        if (orderDTO.getCustomerId() == null) {
            return ResponseEntity.badRequest().body("Order must have a customerId");
        }
        Optional<Customer> customerOptional = customerService.getCustomerById(orderDTO.getCustomerId());
        if (customerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found with id:" + orderDTO.getCustomerId());
        }
        Customer customer = customerOptional.get();

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(orderDTO.getStatus());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setCustomer(customer);

        if (orderDTO.getItems() != null) {
            List<OrderItem> items = orderDTO.getItems().stream().map(dto -> {
                Product product = productService.getProductById(dto.getProductId()).orElse(null);
                if (product == null) throw new RuntimeException("Invalid productId " + dto.getProductId());

                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(dto.getQuantity());
                item.setPrice(dto.getPrice());
                item.setOrder(order);
                return item;
            }).collect(Collectors.toList());
            order.setItems(items);
        }

        Order savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(mapToDTO(savedOrder));
    }

    @Operation(summary = "Delete order", description = "Delete an order by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> {
                    orderService.deleteOrder(order);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    // === ORDER ITEM ENDPOINTS ===
    @Operation(summary = "Add an item to an order", description = "Add a product to an existing order")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<?> addOrderItem(@PathVariable Long orderId,
                                          @RequestBody OrderItemDTO orderItemDTO) {
        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        Optional<Product> productOpt = productService.getProductById(orderItemDTO.getProductId());

        if (orderOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid order or product ID");
        }

        Order order = orderOpt.get();
        Product product = productOpt.get();

        Optional<OrderItem> existingItemOpt = orderItemService.findByOrderAndProduct(orderId, orderItemDTO.getProductId());
        OrderItem item;

        if (existingItemOpt.isPresent()) {
            item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + orderItemDTO.getQuantity());
            item.setPrice(item.getPrice() + product.getPrice() * orderItemDTO.getQuantity());
        } else {
            item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(orderItemDTO.getQuantity());
            item.setPrice(product.getPrice() * orderItemDTO.getQuantity());
        }

        orderItemService.save(item);
        return ResponseEntity.ok(mapToDTO(orderService.saveOrder(order)));
    }

    @Operation(summary = "Remove an item from an order", description = "Delete a specific item from an existing order")
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<?> removeOrderItem(@PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        return orderItemService.findById(itemId)
                .map(item -> {
                    if (!item.getOrder().getId().equals(orderId)) {
                        return ResponseEntity.badRequest().body("Item does not belong to this order");
                    }
                    orderItemService.delete(item);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
