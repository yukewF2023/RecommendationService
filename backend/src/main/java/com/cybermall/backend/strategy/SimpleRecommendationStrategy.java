package com.cybermall.backend.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.service.ProductService;

/**
 * Represents a recommendation strategy that recommends products based on popularity.
 */
public class SimpleRecommendationStrategy implements RecommendationStrategy {
    private ProductService productService;

    public SimpleRecommendationStrategy(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Recommends products to a user based on popularity.
     *
     * @param user The user to recommend products to.
     * @return The list of product IDs recommended to the user.
     */
    @Override
    public List<Long> recommend(User user) {
        System.out.println("Using simple strategy to recommend products based on popularity");
        List<Product> products = productService.getAllProducts();
        // Sort products based on the number of views in descending order
        return products.stream()
                       .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                       .map(Product::getProductId)
                       .collect(Collectors.toList());
    }
}
