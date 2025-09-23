package com.example.ecommerce_api.service;

import com.example.ecommerce_api.dto.ProductDto;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ProductDto createProduct(Object request) {
        try {
            ProductDto productDto = convertToProductDto(request);

            Product product = Product.builder()
                    .name(productDto.getName())
                    .description(productDto.getDescription())
                    .price(productDto.getPrice())
                    .stockQuantity(productDto.getStockQuantity())
                    .category(productDto.getCategory())
                    .build();

            Product savedProduct = productRepository.save(product);
            return convertToDto(savedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDto(product);
    }

    @Override
    public ProductDto updateProduct(Long id, Object request) {
        try {
            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

            ProductDto productDto = convertToProductDto(request);

            existingProduct.setName(productDto.getName());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setStockQuantity(productDto.getStockQuantity());
            existingProduct.setCategory(productDto.getCategory());

            Product updatedProduct = productRepository.save(existingProduct);
            return convertToDto(updatedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsInStock() {
        return productRepository.findProductsInStock().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity() + ", Requested: " + quantity);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    private ProductDto convertToProductDto(Object request) {
        if (request instanceof ProductDto) {
            return (ProductDto) request;
        } else if (request instanceof Map) {
            return objectMapper.convertValue(request, ProductDto.class);
        } else {
            return objectMapper.convertValue(request, ProductDto.class);
        }
    }

    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .product_id(product.getProduct_id())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .build();
    }
}