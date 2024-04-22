package com.cybermall.backend.strategy;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.io.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;
import com.cybermall.backend.service.ProductService;
import com.cybermall.backend.service.PythonScriptInvoker;

/**
 * Represents a recommendation strategy that uses collaborative filtering to recommend products to a user.
 */
public class CollaborativeFilteringStrategy implements RecommendationStrategy {
    private ProductService productService;
    private User user;
    private ViewHistoryRepository viewHistoryRepository;
    private PythonScriptInvoker pythonScriptInvoker;

    public CollaborativeFilteringStrategy(ProductService productService, User user, ViewHistoryRepository viewHistoryRepository, PythonScriptInvoker pythonScriptInvoker) {
        this.productService = productService;
        this.user = user;
        this.viewHistoryRepository = viewHistoryRepository;
        this.pythonScriptInvoker = pythonScriptInvoker;
    }

    /**
     * Converts a list of view histories to a JSON string.
     * 
     * @param viewHistories The list of view histories to convert.
     * @return The JSON string representing the view histories.
     */
    private String convertViewHistoriesToJson(List<ViewHistory> viewHistories) {
        StringBuilder json = new StringBuilder("[");
        for (ViewHistory viewHistory : viewHistories) {
            json.append("{")
                .append("\"user_id\": ").append(viewHistory.getUserId()).append(", ")
                .append("\"product_id\": ").append(viewHistory.getProductId()).append(", ")
                .append("\"number_of_view\": ").append(viewHistory.getNumberOfViews()).append(", ")
                .append("\"price\": ").append(productService.getProductById(viewHistory.getProductId()).getPrice()).append(", ")
                .append("\"category\": \"").append(productService.getProductById(viewHistory.getProductId()).getCategory()).append("\"")
                .append("},");
        }
        json.deleteCharAt(json.length() - 1); // Remove the trailing comma
        json.append("]");
        return json.toString();
    }

    /**
     * Converts a list of products to a JSON string.
     * 
     * @param products The list of products to convert.
     * @return The JSON string representing the products.
     */
    private String convertProductsToJson(List<Product> products) {
        StringBuilder json = new StringBuilder("[");
        for (Product product : products) {
            json.append("{")
                .append("\"product_id\": ").append(product.getProductId()).append(", ")
                .append("\"number_of_view\": ").append(product.getNumberOfViews()).append(", ")
                .append("\"price\": ").append(product.getPrice()).append(", ")
                .append("\"category\": \"").append(product.getCategory()).append("\"")
                .append("},");
        }
        json.deleteCharAt(json.length() - 1); // Remove the trailing comma
        json.append("]");
        return json.toString();
    }

    /**
     * Recommends products to a user using collaborative filtering.
     * 
     * @param user The user to recommend products to.
     * @return A list of recommended product IDs.
     */
    @Override
    public List<Long> recommend(User user) {
        List<ViewHistory> currentUserViewHistories = this.viewHistoryRepository.findByUserId(user.getUserId());

        List<ViewHistory> allViewHistories = this.viewHistoryRepository.findAll();

        List<Product> allProducts = this.productService.getAllProducts();

        System.out.println("Using collaborative filtering strategy to recommend products");
        try {
            String scriptPath = "src/main/java/com/cybermall/backend/python/recommendation_collaborative.py";
            
            String viewHistoriesCurrentUserJson = this.convertViewHistoriesToJson(currentUserViewHistories);
            String viewHistoriesAllJson = this.convertViewHistoriesToJson(allViewHistories);
            String allProductsJson = this.convertProductsToJson(allProducts);

            // Prepare the JSON input for the Python script
            String jsonInput = String.format(
                "{\"user_id\": %d, \"user_view_histories\": %s, \"all_view_histories\": %s, \"all_products\": %s}",
                this.user.getUserId(), viewHistoriesCurrentUserJson, viewHistoriesAllJson, allProductsJson
            );
    
            List<Long> recommendationsInProductIdList = this.pythonScriptInvoker.invokePythonScript(scriptPath, jsonInput);

            System.out.println("Recommendations: " + recommendationsInProductIdList);
            
            // Return the recommended products
            return recommendationsInProductIdList;

        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            ((Throwable) e).printStackTrace();
            // Return default recommendation list in case of failure
            return this.productService.getAllProducts()
                .stream()
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .map(Product::getProductId)
                .collect(Collectors.toList());
        }
    }
}
