package com.cybermall.backend.strategy;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.io.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;
import com.cybermall.backend.service.ProductService;
import com.cybermall.backend.service.PythonScriptInvoker;

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

    @Override
    public List<Long> recommend(User user) {
        List<ViewHistory> currentUserViewHistories = this.viewHistoryRepository.findByUserId(user.getUserId());

        // retrieve all view history, but don't include the current user's view history
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
            
            // System.out.println("JSON Input to Python Script: " + jsonInput);
            List<Long> recommendationsInProductIdList = this.pythonScriptInvoker.invokePythonScript(scriptPath, jsonInput);

            System.out.println("Recommendations: " + recommendationsInProductIdList);
            
            // Return the recommended products
            return recommendationsInProductIdList;
            // return allProducts.stream()
            //     .filter(product -> recommendationsInProductIdList.contains(product.getProductId()))
            //     .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
            //     .collect(Collectors.toList());

        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            // Log the exception and handle it appropriately
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
