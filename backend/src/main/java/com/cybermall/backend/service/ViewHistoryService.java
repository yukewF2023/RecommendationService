package com.cybermall.backend.service;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.ViewHistoryRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing view history of products by users.
 */
@Service
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final UserService userService;
    private final ProductService productService;

    public ViewHistoryService(ViewHistoryRepository viewHistoryRepository, UserService userService, ProductService productService) {
        this.viewHistoryRepository = viewHistoryRepository;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Records a view of a product by a user.
     *
     * @param userId The ID of the user who viewed the product.
     * @param productId The ID of the product being viewed.
     */
    @Transactional
    public void recordView(Long userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);

        productService.updateProductView(productId, product.getNumberOfViews() + 1);

        // Check for existing view history or create a new one
        ViewHistory history = viewHistoryRepository.findByUserIdAndProductId(userId, productId)
            .orElseGet(() -> new ViewHistory(userId, productId));

        // Increment the view count and save
        history.setNumberOfViews(history.getNumberOfViews() + 1);
        viewHistoryRepository.save(history);
    }

    public List<ViewHistory> getViewHistoryByUser(Long userId) {
        return viewHistoryRepository.findByUserId(userId);
    }
}
