// package com.example.ecommerce_api.service.impl;

// import com.example.ecommerce_api.entity.Product;
// import com.example.ecommerce_api.repository.ProductRepository;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class ProductService {
//     private final ProductRepository productRepository;

//     public ProductService(ProductRepository productRepository) {
//         this.productRepository = productRepository;
//     }

//     public List<Product> getAllProducts() {
//         return productRepository.findAll();
//     }

//     public Optional<Product> getProductById(Long id) {
//         return productRepository.findById(id);
//     }

//     public Product saveProduct(Product product) {
//         return productRepository.save(product);
//     }

//     public void deleteProduct(Long id) {
//         productRepository.deleteById(id);
//     }
// }


package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(Object request);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, Object request);
    void deleteProduct(Long id);
    List<ProductDto> getProductsByCategory(String category);
    List<ProductDto> getProductsInStock();
    void updateStock(Long productId, Integer quantity);
}