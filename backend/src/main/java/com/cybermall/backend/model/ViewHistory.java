package com.cybermall.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "view_history")
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;

    private Integer numberOfViews;

    public ViewHistory() {}

    public ViewHistory(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
        this.numberOfViews = 1; // Initialize with 1 view
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProduct(Long productId) {
        this.productId = productId;
    }

    public Integer getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(Integer numberOfViews) {
        this.numberOfViews = numberOfViews;
    }
}
