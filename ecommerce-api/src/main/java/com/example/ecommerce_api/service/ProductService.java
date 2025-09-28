package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.ProductUpdateDTO;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.OrderRepository;
import com.example.ecommerce_api.repository.ProductRepository;
import com.example.ecommerce_api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductUpdateDTO updateDTO) {
        // 1. Find the existing product or throw an exception
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // 2. Store the original price to calculate the difference later
        double oldPrice = product.getPrice();

        // 3. Partially update fields if they are provided in the DTO
        if (updateDTO.getName() != null) {
            product.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            product.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPrice() != null) {
            product.setPrice(updateDTO.getPrice());
        }

        double priceDifference = product.getPrice() - oldPrice;
        if (priceDifference != 0) {
            for (Order order : product.getOrders()) {
                order.setTotalPrice(order.getTotalPrice() + priceDifference);
            }
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Order addProductToOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        order.getProducts().add(product);
        order.setTotalPrice(order.getTotalPrice() + product.getPrice());

        return orderRepository.save(order);
    }

    public Set<Product> getProductsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return order.getProducts();
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Create a copy to avoid ConcurrentModificationException
        Set<Order> affectedOrders = new HashSet<>(product.getOrders());

        for (Order order : affectedOrders) {
            order.setTotalPrice(order.getTotalPrice() - product.getPrice());
            order.getProducts().remove(product);
            orderRepository.save(order);
        }
        productRepository.delete(product);
    }
}