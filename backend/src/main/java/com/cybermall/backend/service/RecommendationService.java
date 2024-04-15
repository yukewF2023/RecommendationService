package com.cybermall.backend.service;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ProductRepository productRepository;
    private final MockDataService mockDataService;

    public RecommendationService(ProductRepository productRepository, MockDataService mockDataService) {
        this.productRepository = productRepository;
        this.mockDataService = mockDataService;
    }

    public List<Product> recommendProducts(Long userId) {
        // Simple mock strategy: return products sorted by number of views
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .collect(Collectors.toList());
    }

    // Additional recommendation strategies go here...
    public List<Product> recommendUsingSimpleStrategy() {
        return this.mockDataService.getPopularityBasedProducts();
    }

    public List<Product> recommendUsingMLStrategy() {
        return this.mockDataService.getMLBasedProducts();
    }
}

