package com.example.ecommerce_api.service;

import com.example.ecommerce_api.DTO.ProductDTO;
import com.example.ecommerce_api.entity.Category;
import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.CategoryRepository;
import com.example.ecommerce_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setPrice(product.getPrice());
            existing.setStock(product.getStock());
            existing.setCategory(product.getCategory());
            return productRepository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Optional<Product> findById(Long productId){
        return productRepository.findById(productId);
    }


}
