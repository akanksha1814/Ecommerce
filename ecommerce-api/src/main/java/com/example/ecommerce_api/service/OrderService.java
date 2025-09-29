package com.example.ecommerce_api.service;

import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.OrderItem;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.CustomerRepository;
import com.example.ecommerce_api.repository.OrderRepository;
import com.example.ecommerce_api.exception.ResourceNotFoundException;
import com.example.ecommerce_api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository=productRepository;
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

        // Only return stock for orders that were not finalized
        if (!"PLACED".equals(order.getStatus()) && !"DELIVERED".equals(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    product.setStock(product.getStock() + item.getQuantity());
                }
            }
        }
        orderRepository.delete(order);
        return orderId;
    }

    @Transactional
    public void deleteAllOrdersByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        for (Order order : orders) {
            // Use the same logic as single order deletion to return stock
            if (!"PLACED".equals(order.getStatus()) && !"DELIVERED".equals(order.getStatus())) {
                for (OrderItem item : order.getItems()) {
                    Product product = item.getProduct();
                    if (product != null) {
                        product.setStock(product.getStock() + item.getQuantity());
                    }
                }
            }
        }
        orderRepository.deleteAll(orders);
    }

    @Transactional
    public Order removeProductFromOrder( Long orderId, Long productId) {
        // 1. Find the specific order for the given customer.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with ID " + orderId + " not found "
                ));
        // 2. Check if the order can be modified.
        if ("PLACED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot modify an order that has been placed or delivered.");
        }

        // 3. Find the specific OrderItem within the order that corresponds to the productId.
        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + productId + " not found in Order with ID " + orderId
                ));

        Product product = itemToRemove.getProduct();

        // 4. Update order total (using the historical price) and product stock.
        order.setTotalPrice(order.getTotalPrice() - itemToRemove.getPriceAtPurchase());
        product.setStock(product.getStock() + 1);

        // 5. Decide whether to decrement quantity or remove the item completely.
        if (itemToRemove.getQuantity() > 1) {
            itemToRemove.setQuantity(itemToRemove.getQuantity() - 1);
        } else {
            // Quantity is 1, so remove the entire OrderItem.
            // `orphanRemoval=true` on the Order entity ensures this item is deleted from the database.
            order.getItems().remove(itemToRemove);
        }

        return orderRepository.save(order);
    }
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return order.getItems();
    }

    public Order placeOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this customer."));
        if (!"CREATED".equals(order.getStatus())) {
            throw new IllegalStateException("Only orders with status CREATED can be placed.");
        }
        order.setStatus("PLACED");
        return orderRepository.save(order);
    }

    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        if (!"PLACED".equals(order.getStatus())) {
            throw new IllegalStateException("Only orders with status PLACED can be delivered.");
        }
        order.setStatus("DELIVERED");
        return orderRepository.save(order);
    }
}