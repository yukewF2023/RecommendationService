package com.cybermall.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.service.*;
import com.cybermall.backend.strategy.*;
import com.cybermall.backend.repository.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationServiceController {

    private final UserService userService;
    private final ViewHistoryRepository viewHistoryRepository;
    private final ViewHistoryService viewHistoryService;
    private final ProductService productService;
    private RecommendationStrategy currentStrategy;

    public RecommendationServiceController(UserService userService,
                                           ViewHistoryRepository viewHistoryRepository,
                                           ViewHistoryService viewHistoryService,
                                           ProductService productService) {
        this.userService = userService;
        this.viewHistoryRepository = viewHistoryRepository;
        this.viewHistoryService = viewHistoryService;
        this.productService = productService;
    }

    private void setStrategy(User user) {
        int totalProducts = this.productService.getAllProducts().size();
        int totalUniqueProductsViewedByCurrentUser = viewHistoryService.getViewHistoryByUser(user.getUserId()).size();

        // Adjust thresholds based on catalog size
        int basicThreshold = Math.min(10, totalProducts / 10); // Ensure threshold doesn't exceed 10% of catalog
        int advancedThreshold = Math.min(30, totalProducts / 8); // Ensure threshold doesn't exceed 25% of catalog

        if (totalProducts <= 10 || totalUniqueProductsViewedByCurrentUser < basicThreshold) {
            currentStrategy = new SimpleRecommendationStrategy(this.productService);
        } else if (totalUniqueProductsViewedByCurrentUser >= basicThreshold && totalUniqueProductsViewedByCurrentUser < advancedThreshold) {
            PythonScriptInvoker pythonInvoker = new PythonScriptInvoker();
            currentStrategy = new ContentBasedRecommendationStrategy(this.productService, user, this.viewHistoryRepository, pythonInvoker);
        } else {
            PythonScriptInvoker pythonInvoker = new PythonScriptInvoker();
            currentStrategy = new CollaborativeFilteringStrategy(this.productService, user, this.viewHistoryRepository, pythonInvoker);
        }
    }

    /**
     * Returns a list of recommended products based on the given user's preferences and behavior.
     *
     * @param userId the ID of the user for whom to retrieve recommendations
     * @return a list of recommended products, or an error response if the user could not be found
     */
    @GetMapping("/")
    public ResponseEntity<List<Product>> getRecommendations(@RequestParam Long userId) {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            // throw reception error
            throw new RuntimeException("User not found!");
        }
        this.setStrategy(user);
        List<Product> recommendations = currentStrategy.recommend(user);
        return ResponseEntity.ok(recommendations);
    }
}
