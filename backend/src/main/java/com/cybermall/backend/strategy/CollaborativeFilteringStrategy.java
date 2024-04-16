package com.cybermall.backend.strategy;

import java.util.ArrayList;
import java.util.List;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;

public class CollaborativeFilteringStrategy implements RecommendationStrategy {
    private ProductRepository productRepository;
    private User user;
    private ViewHistoryRepository viewHistoryRepository;

    public CollaborativeFilteringStrategy(ProductRepository productRepository, User user, ViewHistoryRepository viewHistoryRepository) {
        this.productRepository = productRepository;
        this.user = user;
        this.viewHistoryRepository = viewHistoryRepository;
    }

    @Override
    public List<Product> recommend(User user) {
        System.out.println("Using collaborative filtering strategy to recommend products");
        // Implement collaborative filtering logic
        return new ArrayList<>();
    }
}