package com.cybermall.backend.service;

import com.cybermall.backend.model.*;
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

    public List<Product> recommendUsingSimpleStrategy() {
        // Use real strategy: return products sorted by number of views using actual data
        System.out.println("Using simple strategy to recommend products based on popularity");
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .collect(Collectors.toList());
    }

    public List<Product> recommendUsingContentBasedStrategy(User user) {
        // Placeholder - replace this with actual content-based logic
        System.out.println("Using content-based strategy to recommend products");
        // Potentially filter by categories or attributes that the user has shown interest in
        return productRepository.findAll();
    }

    public List<Product> recommendUsingCollaborativeFilteringStrategy(User user) {
        // Placeholder - replace this with actual collaborative filtering logic
        System.out.println("Using collaborative filtering strategy to recommend products");
        // Implement a system that looks at user similarities and item similarities
        return productRepository.findAll();
    }
}
