package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.ProductCreateDTO;
import com.example.ecommerce_api.dto.ProductUpdateDTO;
import com.example.ecommerce_api.entity.Category;
import com.example.ecommerce_api.entity.Order;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.categoryRepository=categoryRepository;
    }

    @Transactional
    public Product createProduct(ProductCreateDTO dto) {
        // 1. Find the category by the ID from the DTO
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot create product. Category not found with id: " + dto.getCategoryId()
                ));

        // 2. Create the new Product entity
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category); // 3. Associate the found category

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
        if (updateDTO.getStock() != null) {
            product.setStock(updateDTO.getStock());
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

    // Inside ProductService.java

    @Transactional
    public Order addProductToOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // New Business Logic
        if ("PLACED".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot modify an order that has already been placed.");
        }
        if (product.getStock() <= 0) {
            throw new IllegalStateException("Product " + product.getName() + " is out of stock.");
        }

        order.getProducts().add(product);
        order.setTotalPrice(order.getTotalPrice() + product.getPrice());
        product.setStock(product.getStock() - 1); // Decrement stock

        return orderRepository.save(order);
    }

    public Set<Product> getProductsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return order.getProducts();
    }

//    @Transactional
//    public void deleteProduct(Long id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//
//        // Create a copy to avoid ConcurrentModificationException
//        Set<Order> affectedOrders = new HashSet<>(product.getOrders());
//
//        for (Order order : affectedOrders) {
//            String status = order.getStatus();
//            if (status != null && !status.equals("PLACED") && !status.equals("DELIVERED")) {
//
//                // This logic now only runs for modifiable orders
//                order.setTotalPrice(order.getTotalPrice() - product.getPrice());
//                order.getProducts().remove(product);
//                // No need to save order individually; @Transactional handles it.
//            }
//        }
//        productRepository.delete(product);
//    }
    @Transactional

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)

                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));


        Set<Order> affectedOrders = new HashSet<>(product.getOrders());

        for (Order order : affectedOrders) {
            order.setTotalPrice(order.getTotalPrice() - product.getPrice());
            order.getProducts().remove(product);
            orderRepository.save(order);
        }
        productRepository.delete(product);
    }
    public List<Product> getProductsByCategoryId(Long categoryId) {
        // 1. Find the category or throw an exception if it doesn't exist.
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // 2. Return the list of products associated with this category.
        return category.getProducts();
    }
}