package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.repository.CustomerRepository;
import com.example.ecommerce_api.repository.OrderRepository;
import com.example.ecommerce_api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }
    public Order getCustomerOrderById(Long customerId, Long orderId) {
        // The custom repository method ensures we only find the order
        // if it belongs to the correct customer.
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with ID " + orderId + " not found for Customer with ID " + customerId
                ));
    }

    public Order createOrder(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        // Use the constructor instead of the builder
        Order newOrder = new Order(0.0, "CREATED", customer);

        return orderRepository.save(newOrder);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        return orderRepository.findByCustomerId(customerId);
    }

    public Long deleteCustomerOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found for customer " + customerId));
        orderRepository.delete(order);
        return orderId;
    }

    @Transactional
    public void deleteAllOrdersByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        orderRepository.deleteAllByCustomerId(customerId);
    }
}