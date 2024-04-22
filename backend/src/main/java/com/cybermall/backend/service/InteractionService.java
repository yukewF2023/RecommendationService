package com.cybermall.backend.service;

import org.springframework.stereotype.Service;

import com.cybermall.backend.model.*;

/**
 * Service for receiving user interactions with products.
 */
@Service
public class InteractionService {

    private final ViewHistoryService viewHistoryService;
    private final ProductService productService;

    /**
     * Constructs an instance of the InteractionService class.
     *
     * @param viewHistoryService The service for recording view history.
     * @param productService The service for retrieving product details.
     */
    public InteractionService(ViewHistoryService viewHistoryService, ProductService productService) {
        this.viewHistoryService = viewHistoryService;
        this.productService = productService;
    }

    /**
     * Simulates a product view by recording the view history and retrieving the product details.
     *
     * @param userId The ID of the user who viewed the product.
     * @param productId The ID of the product being viewed.
     * @return The product details.
     */
    public Product simulateProductView(Long userId, Long productId) {
        viewHistoryService.recordView(userId, productId);
        // After recording the view, retrieve and return the product details
        return this.productService.getProductById(productId);
    }
}

