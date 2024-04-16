package com.cybermall.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;

@Service
public class DataRetrieverService {

    private final UserService userService;
    private final ProductService productService;
    private final ViewHistoryService viewHistoryService;
    private final OrderService orderService;

    public DataRetrieverService(UserService userService, ProductService productService, ViewHistoryService viewHistoryService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.viewHistoryService = viewHistoryService;
        this.orderService = orderService;
    }

    public RecommendationData retrieveDataForRecommendation(Long userId) {
        // Fetch the actual user from the database
        User user = userService.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found."));

        // Gather all necessary data for recommendations
        RecommendationData data = new RecommendationData(user);
        data.setProducts(productService.getAllProducts());
        data.setViewHistory(viewHistoryService.getViewHistoryByUser(user));
        data.setOrders(orderService.getOrdersByUser(user));

        return data;
    }
}
