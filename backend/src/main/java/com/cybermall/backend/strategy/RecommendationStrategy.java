package com.cybermall.backend.strategy;

import com.cybermall.backend.model.*;
import java.util.List;

/**
 * Represents a recommendation strategy that recommends products to a user.
 */
public interface RecommendationStrategy {
    List<Long> recommend(User user);
}
