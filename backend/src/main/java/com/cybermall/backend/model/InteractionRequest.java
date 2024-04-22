package com.cybermall.backend.model;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to record a user interaction with a product.
 */
public class InteractionRequest {
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    public InteractionRequest() {}

    public InteractionRequest(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
