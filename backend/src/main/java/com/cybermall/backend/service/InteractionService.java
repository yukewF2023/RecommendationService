package com.cybermall.backend.service;

import org.springframework.stereotype.Service;

import com.cybermall.backend.model.*;

@Service
public class InteractionService {

    private final ViewHistoryService viewHistoryService;
    private final OrderService orderService;
    private final ProductService productService;

    public InteractionService(ViewHistoryService viewHistoryService, OrderService orderService, ProductService productService) {
        this.viewHistoryService = viewHistoryService;
        this.orderService = orderService;
        this.productService = productService;
    }

    public Product simulateProductView(Long userId, Long productId) {
        viewHistoryService.recordView(userId, productId);
        // After recording the view, retrieve and return the product details
        return this.productService.getProductById(productId);
    }

    public Order simulateProductOrder(Long userId, Long productId) {
        Order order = this.orderService.recordOrder(userId, productId);
        // After placing the order, retrieve and return the order details
        return order;
    }
}

