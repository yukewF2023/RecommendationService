package com.cybermall.backend.service;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.model.ViewHistory;
import com.cybermall.backend.repository.ProductRepository;
import com.cybermall.backend.repository.UserRepository;
import com.cybermall.backend.repository.ViewHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DataRetrieverService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ViewHistoryRepository viewHistoryRepository;

    public DataRetrieverService(UserRepository userRepository, ProductRepository productRepository, ViewHistoryRepository viewHistoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.viewHistoryRepository = viewHistoryRepository;
    }

    public Set<ViewHistory> getUserViewHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getViewHistories();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Other methods to interact with OrderRepository if needed.
}
