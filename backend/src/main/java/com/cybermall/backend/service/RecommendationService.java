package com.cybermall.backend.service;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ProductRepository productRepository;

    public RecommendationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> recommendProducts() {
        // Use real strategy: return products sorted by number of views using actual data
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .collect(Collectors.toList());
    }

    // Additional recommendation strategies go here...
    public List<Product> recommendUsingSimpleStrategy() {
        // Implement the logic using real data
        // For now, it's the same as the recommendProducts method, but you can customize it later
        System.out.println("Using simple strategy to recommend products");
        return recommendProducts();
    }

    public List<Product> recommendUsingMLStrategy(User user) {
        // Placeholder for ML strategy - you will replace this with actual call to ML model
        // For now, just return all products as a placeholder
        System.out.println("Using ML strategy to recommend products");
        return productRepository.findAll();
    }
}