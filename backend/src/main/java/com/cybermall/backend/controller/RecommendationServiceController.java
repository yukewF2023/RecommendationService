package com.cybermall.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.service.*;
import com.cybermall.backend.strategy.*;
import com.cybermall.backend.repository.*;

/**
 * The controller class for the Recommendation Service API.
 * Handles requests related to retrieving recommended products based on user preferences and behavior.
 */
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

    /**
     * Sets the recommendation strategy based on the user's behavior and preferences.
     *
     * @param user the user for whom to set the recommendation strategy
     */
    private void setStrategy(User user) {
        int totalProducts = this.productService.getAllProducts().size();
        int totalUniqueProductsViewedByCurrentUser = viewHistoryService.getViewHistoryByUser(user.getUserId()).size();

        // Adjust thresholds based on catalog size
        int basicThreshold = Math.min(10, totalProducts / 7); // Ensure threshold doesn't exceed 15% of catalog
        int advancedThreshold = Math.min(30, totalProducts / 3); // Ensure threshold doesn't exceed 33% of catalog

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
    @PostMapping("/")
    public ResponseEntity<List<Long>> getRecommendations(@RequestParam Long userId) {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            // throw exception error
            throw new RuntimeException("User not found!");
        }
        this.setStrategy(user);
        List<Long> recommendations = currentStrategy.recommend(user);
        return ResponseEntity.ok(recommendations);
    }
}
