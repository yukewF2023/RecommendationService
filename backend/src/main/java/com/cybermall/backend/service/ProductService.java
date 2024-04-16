package com.cybermall.backend.service;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.repository.ProductRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Method to retrieve product by id or other criteria
    public Product getProductById(Long productId) {
        return this.productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found!"));
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

}
