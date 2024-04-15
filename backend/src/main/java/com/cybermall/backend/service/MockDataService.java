package com.cybermall.backend.service;

import com.cybermall.backend.model.Order;
import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.model.ViewHistory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class MockDataService {

    private final List<Product> products = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<ViewHistory> viewHistories = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong productIdCounter = new AtomicLong();
    private final AtomicLong userIdCounter = new AtomicLong();
    private final AtomicLong orderIdCounter = new AtomicLong();
    
    public MockDataService() {
        // Initialize with some fake data
        users.add(new User("yuketest2"));
        users.add(new User("yuketest3"));
        
        products.add(new Product("Bracelet", "Description 1", 30.0, "url1", "jewelry"));
        products.add(new Product("Necklace", "Description 2", 40.0, "url2", "jewelry"));
        // products.forEach(p -> p.setId(productIdCounter.incrementAndGet()));

        // users.forEach(u -> u.setId(userIdCounter.incrementAndGet()));
    }

    public List<Product> getFakeProducts() {
        return products;
    }

    public List<User> getFakeUsers() {
        return users;
    }

    public List<ViewHistory> getFakeViewHistories() {
        return viewHistories;
    }

    public List<Order> getFakeOrders() {
        return orders;
    }

    public Product addFakeView(Long userId, Long productId) {
        Product product = products.stream()
                                  .filter(p -> Objects.equals(p.getProductId(), productId))
                                  .findFirst()
                                  .orElse(null);

        if (product != null) {
            Optional<ViewHistory> existingHistory = viewHistories.stream()
                .filter(vh -> Objects.equals(vh.getUser().getUserId(), userId) &&
                               Objects.equals(vh.getProduct().getProductId(), productId))
                .findFirst();

            if (existingHistory.isPresent()) {
                existingHistory.get().setNumberOfViews(existingHistory.get().getNumberOfViews() + 1);
            } else {
                User user = users.stream()
                                 .filter(u -> Objects.equals(u.getUserId(), userId))
                                 .findFirst()
                                 .orElse(null);

                if (user != null) {
                    ViewHistory newViewHistory = new ViewHistory(user, product);
                    // newViewHistory.setId(orderIdCounter.incrementAndGet());
                    viewHistories.add(newViewHistory);
                }
            }
            // Increment the number of views for the product
            product.setNumberOfViews(product.getNumberOfViews() + 1);
        }

        return product;
    }

    public Order addFakeOrder(Long userId, Long productId) {
        Product product = products.stream()
                                  .filter(p -> Objects.equals(p.getProductId(), productId))
                                  .findFirst()
                                  .orElse(null);

        User user = users.stream()
                         .filter(u -> Objects.equals(u.getUserId(), userId))
                         .findFirst()
                         .orElse(null);

        if (product != null && user != null) {
            Order order = new Order(user, product);
            // order.setId(orderIdCounter.incrementAndGet());
            orders.add(order);
            return order;
        }
        return null;
    }

    // Method to retrieve the popularity list of products based on views
    public List<Product> getPopularityBasedProducts() {
        return products.stream()
                       .sorted(Comparator.comparing(Product::getNumberOfViews).reversed())
                       .collect(Collectors.toList());
    }

    // method to retrieve ML based products
    // for now just return 1 product
    public List<Product> getMLBasedProducts() {
        return Collections.singletonList(products.get(0));
    }

    // Use this method to reset the isNewUser status based on view count
    public void updateNewUserStatus() {
        users.forEach(user -> {
            int viewCount = (int) viewHistories.stream()
                .filter(vh -> Objects.equals(vh.getUser().getUserId(), user.getUserId()))
                .count();
            user.setIsNewUser(viewCount <= 10);
        });
    }
}
