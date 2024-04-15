package com.cybermall.backend.service;

import org.springframework.stereotype.Service;

import com.cybermall.backend.model.Order;
import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.repository.*;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void recordOrder(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new RuntimeException("Product not found."));

        Order order = new Order(user, product);
        orderRepository.save(order);
    }

    // Other methods...
}