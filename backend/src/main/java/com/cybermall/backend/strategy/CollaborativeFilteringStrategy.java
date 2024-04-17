package com.cybermall.backend.strategy;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.io.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;
import com.cybermall.backend.service.PythonScriptInvoker;

public class CollaborativeFilteringStrategy implements RecommendationStrategy {
    private ProductRepository productRepository;
    private User user;
    private ViewHistoryRepository viewHistoryRepository;
    private PythonScriptInvoker pythonScriptInvoker;
    private OrderRepository orderRepository;

    public CollaborativeFilteringStrategy(ProductRepository productRepository, User user, ViewHistoryRepository viewHistoryRepository, PythonScriptInvoker pythonScriptInvoker, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.user = user;
        this.viewHistoryRepository = viewHistoryRepository;
        this.pythonScriptInvoker = pythonScriptInvoker;
        this.orderRepository = orderRepository;
    }
    private String convertViewHistoriesToJson(List<ViewHistory> viewHistories) {
        StringBuilder json = new StringBuilder("[");
        for (ViewHistory viewHistory : viewHistories) {
            json.append("{")
                .append("\"user_id\": ").append(viewHistory.getUser().getUserId()).append(", ")
                .append("\"product_id\": ").append(viewHistory.getProduct().getProductId()).append(", ")
                .append("\"number_of_view\": ").append(viewHistory.getNumberOfViews()).append(", ")
                .append("\"price\": ").append(viewHistory.getProduct().getPrice()).append(", ")
                .append("\"category\": \"").append(viewHistory.getProduct().getCategory()).append("\"")
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

    private String convertOrdersToJson(List<Order> orders) {
        StringBuilder json = new StringBuilder("[");
        for (Order order : orders) {
            json.append("{")
                .append("\"product_id\": ").append(order.getProduct().getProductId()).append(", ")
                .append("\"price\": ").append(order.getProduct().getPrice()).append(", ")
                .append("\"category\": \"").append(order.getProduct().getCategory()).append("\"")
                .append("},");
        }
        json.deleteCharAt(json.length() - 1); // Remove the trailing comma
        json.append("]");
        return json.toString();
    }

    @Override
    public List<Product> recommend(User user) {
        List<ViewHistory> currentUserViewHistories = this.viewHistoryRepository.findByUserId(user.getUserId());

        // retrieve all view history, but don't include the current user's view history
        List<ViewHistory> allViewHistories = this.viewHistoryRepository.findAll();

        List<Order> currentUserOrders = this.orderRepository.findByUserId(user.getUserId());

        // retrieve all orders, but don't include the current user's orders
        List<Order> allOrders = this.orderRepository.findAll();
        allOrders.removeAll(currentUserOrders);

        List<Product> allProducts = this.productRepository.findAll();

        System.out.println("Using collaborative filtering strategy to recommend products");
        try {
            String scriptPath = "src/main/java/com/cybermall/backend/python/recommendation_collaborative.py";
            
            String viewHistoriesCurrentUserJson = this.convertViewHistoriesToJson(currentUserViewHistories);
            String viewHistoriesAllJson = this.convertViewHistoriesToJson(allViewHistories);
            String ordersCurrentUserJson = this.convertOrdersToJson(currentUserOrders);
            String ordersAllJson = this.convertOrdersToJson(allOrders);
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
            return allProducts.stream()
                .filter(product -> recommendationsInProductIdList.contains(product.getProductId()))
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .collect(Collectors.toList());

        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            // Log the exception and handle it appropriately
            ((Throwable) e).printStackTrace();
            // Return default recommendation list in case of failure
            return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                .collect(Collectors.toList());
        }
    }
}
