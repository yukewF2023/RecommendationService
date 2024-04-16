package com.cybermall.backend.service;

import com.cybermall.backend.model.*;
import com.cybermall.backend.repository.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ViewHistoryService(ViewHistoryRepository viewHistoryRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.viewHistoryRepository = viewHistoryRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void recordView(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found."));

        // Increment the product's view count
        product.setNumberOfViews(product.getNumberOfViews() + 1);
        productRepository.save(product);

        // Update the view history
        ViewHistory history = viewHistoryRepository.findByUserIdAndProductId(userId, productId)
            .orElseGet(() -> new ViewHistory(user, product));  // Start count at 0 for a new view history record

        history.setNumberOfViews(history.getNumberOfViews() + 1);
        viewHistoryRepository.save(history);

        // Determine if the user is still new or ready for content-based filtering
        updateIsNewUser(userId);
    }

    private void updateIsNewUser(Long userId) {
        int uniqueProductsViewed = viewHistoryRepository.findByUserId(userId).size();
        if (uniqueProductsViewed >= 10) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
            user.setIsNewUser(false);  // Assuming `isNewUser` now means eligible for more personalized recommendations
            userRepository.save(user);
        }
    }

    public List<ViewHistory> getViewHistoryByUser(User user) {
        return viewHistoryRepository.findByUserId(user.getUserId());
    }
}