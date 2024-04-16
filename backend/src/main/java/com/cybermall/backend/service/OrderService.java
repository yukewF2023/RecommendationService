package com.cybermall.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cybermall.backend.model.*;
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
    public Order recordOrder(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new RuntimeException("Product not found."));

        Order order = new Order(user, product);
        this.orderRepository.save(order);
        return order;
    }

    public List<Order> getOrdersByUser(User user) {
        return this.orderRepository.findByUserId(user.getUserId());
    }
}