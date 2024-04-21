package com.cybermall.backend.model;

import java.util.ArrayList;
import java.util.List;

public class RecommendationData {
    private User user;
    private List<Product> products;
    private List<ViewHistory> viewHistory;

    // Default constructor for JPA
    public RecommendationData() {}

    // Constructor, getters, and setters
    public RecommendationData(User user) {
        this.user = user;
        this.products = new ArrayList<Product>();
        this.viewHistory = new ArrayList<ViewHistory>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ViewHistory> getViewHistory() {
        return viewHistory;
    }

    public void setViewHistory(List<ViewHistory> viewHistory) {
        this.viewHistory = viewHistory;
    }
}