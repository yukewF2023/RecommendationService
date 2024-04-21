package com.cybermall.backend.service;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.ViewHistoryRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void recordView(Long userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);

        // Assuming the external service will handle updating view counts
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
