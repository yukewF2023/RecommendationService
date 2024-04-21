package com.cybermall.backend.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.service.ProductService;

public class SimpleRecommendationStrategy implements RecommendationStrategy {
    private ProductService productService;

    public SimpleRecommendationStrategy(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<Long> recommend(User user) {
        System.out.println("Using simple strategy to recommend products based on popularity");
        // Fetch all products using ProductService which interacts with external API
        List<Product> products = productService.getAllProducts();
        // Sort products based on the number of views in descending order
        // return products.stream()
        //                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
        //                .collect(Collectors.toList());
        return products.stream()
                       .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                       .map(Product::getProductId)
                       .collect(Collectors.toList());
    }
}
