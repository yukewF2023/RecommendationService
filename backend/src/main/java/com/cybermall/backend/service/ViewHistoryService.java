package com.cybermall.backend.service;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.model.ViewHistory;
import com.cybermall.backend.repository.ProductRepository;
import com.cybermall.backend.repository.UserRepository;
import com.cybermall.backend.repository.ViewHistoryRepository;

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
        // Fetch the existing User and Product from the database
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new RuntimeException("Product not found."));

        // Check for an existing view history or create a new one
        ViewHistory history = viewHistoryRepository.findByUserIdAndProductId(userId, productId)
            .orElseGet(() -> {
                ViewHistory newHistory = new ViewHistory(user, product);
                newHistory.setNumberOfViews(0); // Start the count at 0
                return newHistory;
            });

        // Increment the view count and save
        history.setNumberOfViews(history.getNumberOfViews() + 1);
        viewHistoryRepository.save(history);
    }
}