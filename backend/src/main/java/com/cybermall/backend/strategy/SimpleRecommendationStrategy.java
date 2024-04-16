package com.cybermall.backend.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.ProductRepository;

public class SimpleRecommendationStrategy implements RecommendationStrategy {
    private ProductRepository productRepository;

    public SimpleRecommendationStrategy(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> recommend(User user) {
        System.out.println("Using simple strategy to recommend products based on popularity");
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .collect(Collectors.toList());
    }
}
