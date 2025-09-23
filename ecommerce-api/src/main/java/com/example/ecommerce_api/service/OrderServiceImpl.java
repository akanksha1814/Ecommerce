package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.OrderDto;
import com.example.ecommerce_api.dto.OrderItemDto;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.OrderItem;
import com.example.ecommerce_api.repository.CustomerRepository;
import com.example.ecommerce_api.repository.OrderItemRepository;
import com.example.ecommerce_api.repository.OrderRepository;
import com.example.ecommerce_api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @Override
    public OrderDto createOrder(Object request) {
        try {
            OrderDto orderDto = convertToOrderDto(request);

            // Validate customer exists
            if (!customerRepository.existsById(orderDto.getCustomer_id())) {
                throw new RuntimeException("Customer not found with id: " + orderDto.getCustomer_id());
            }

            // Calculate total amount and validate products
            double totalAmount = 0.0;
            for (OrderItemDto item : orderDto.getOrderItems()) {
                if (!productRepository.existsById(item.getProduct_id())) {
                    throw new RuntimeException("Product not found with id: " + item.getProduct_id());
                }

                // Update stock
                productService.updateStock(item.getProduct_id(), item.getQuantity());

                totalAmount += item.getQuantity() * item.getUnitPrice();
            }

            // Create order
            Order order = Order.builder()
                    .totalAmount(totalAmount)
                    .orderStatus("PENDING")
                    .orderDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            Order savedOrder = orderRepository.save(order);

            // Create order items
            List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                    .map(itemDto -> OrderItem.builder()


                            .quantity(itemDto.getQuantity())
                            .unitPrice(itemDto.getUnitPrice())
                            .build())
                    .collect(Collectors.toList());

            orderItemRepository.saveAll(orderItems);

            return convertToDto(savedOrder, orderItems);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(order.getOrder_id());
                    return convertToDto(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(id);
        return convertToDto(order, orderItems);
    }

    @Override
    public OrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setOrderStatus(status.toUpperCase());
        Order updatedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(id);
        return convertToDto(updatedOrder, orderItems);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        // Delete order items first
        orderItemRepository.deleteByOrder_Id(id);
        // Delete order
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomer_CustomerId(customerId);
        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(order.getOrder_id());
                    return convertToDto(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findByOrderStatus(status.toUpperCase());
        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(order.getOrder_id());
                    return convertToDto(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    private OrderDto convertToOrderDto(Object request) {
        if (request instanceof OrderDto) {
            return (OrderDto) request;
        } else if (request instanceof Map) {
            return objectMapper.convertValue(request, OrderDto.class);
        } else {
            return objectMapper.convertValue(request, OrderDto.class);
        }
    }

    private OrderDto convertToDto(Order order, List<OrderItem> orderItems) {
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(item -> OrderItemDto.builder()
                        .orderItem_id(item.getOrder_item_id())

                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.builder()
                .order_id(order.getOrder_id())

                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .orderItems(orderItemDtos)
                .build();
    }
}