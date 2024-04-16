package com.cybermall.backend.strategy;

import java.util.ArrayList;
import java.util.List;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;

public class ContentBasedRecommendationStrategy implements RecommendationStrategy {
    private ProductRepository productRepository;
    private User user;
    private ViewHistoryRepository viewHistoryRepository;

    public ContentBasedRecommendationStrategy(ProductRepository productRepository, User user, ViewHistoryRepository viewHistoryRepository) {
        this.productRepository = productRepository;
        this.user = user;
        this.viewHistoryRepository = viewHistoryRepository;
    }

    @Override
    public List<Product> recommend(User user) {
        System.out.println("Using content-based strategy to recommend products");
        // Implement content-based recommendation logic
        return new ArrayList<>();
    }
}
