package com.cybermall.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cybermall.backend.model.Order;
import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.RecommendationData;
import com.cybermall.backend.model.User;
import com.cybermall.backend.model.ViewHistory;

@Service
public class DataRetrieverService {

    private final UserService userService;
    private final ProductService productService;
    private final ViewHistoryService viewHistoryService;
    private final MockDataService mockDataService;

    public DataRetrieverService(UserService userService, ProductService productService, ViewHistoryService viewHistoryService, MockDataService mockDataService) {
        this.userService = userService;
        this.productService = productService;
        this.viewHistoryService = viewHistoryService;
        this.mockDataService = mockDataService;
    }

    public RecommendationData retrieveDataForRecommendation(User user) {
        // Create an instance of RecommendationData
        RecommendationData data = new RecommendationData(user);
        
        // Add mock data from MockDataService for testing
        data.setUser(mockDataService.getFakeUsers().stream().filter(u -> u.getUserId().equals(user.getUserId())).findFirst().orElse(null));
        data.setProducts(mockDataService.getFakeProducts());
        data.setViewHistory(mockDataService.getFakeViewHistories());
        data.setOrders(mockDataService.getFakeOrders());

        return data;
    }
}
