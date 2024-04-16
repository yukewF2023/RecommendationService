package com.cybermall.backend.strategy;

import com.cybermall.backend.model.*;
import java.util.List;

public interface RecommendationStrategy {
    List<Product> recommend(User user);
}
