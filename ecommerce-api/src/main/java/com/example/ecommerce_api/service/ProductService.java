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

import com.example.ecommerce_api.DTO.ProductDTO;
import com.example.ecommerce_api.DTO.ProductRequestDTO;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    // Basic CRUD operations
    ProductDTO createProduct(ProductRequestDTO productRequestDTO);
    Optional<ProductDTO> getProductById(Long id);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long id);

    // Enhanced operations
    List<ProductDTO> getProductsByCategory(Long categoryId);
    Page<ProductDTO> getProductsByCategoryPaginated(Long categoryId, Pageable pageable);
    List<ProductDTO> searchProductsByName(String name);
    List<ProductDTO> getProductsByPriceRange(Double minPrice, Double maxPrice);
    List<ProductDTO> getLowStockProducts(Integer threshold);
    List<ProductDTO> getInStockProducts();
    List<ProductDTO> getProductsByCategoryName(String categoryName);

    // Stock management
    ProductDTO updateStock(Long id, Integer newStock);
    ProductDTO addStock(Long id, Integer additionalStock);
    ProductDTO reduceStock(Long id, Integer reduceBy);

    // Utility methods
    boolean existsById(Long id);
    Long countProductsByCategory(Long categoryId);
}
