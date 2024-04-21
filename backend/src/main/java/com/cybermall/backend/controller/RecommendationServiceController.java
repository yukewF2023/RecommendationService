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

    private final DataRetrieverService dataRetrieverService;
    private final UserRepository userRepository;
    private final ViewHistoryRepository viewHistoryRepository;
    private final ViewHistoryService viewHistoryService;
    private final ProductRepository productRepository;
    private RecommendationStrategy currentStrategy;

    public RecommendationServiceController(DataRetrieverService dataRetrieverService,
                                           UserRepository userRepository,
                                           ViewHistoryRepository viewHistoryRepository,
                                           ViewHistoryService viewHistoryService,
                                           ProductRepository productRepository) {
        this.dataRetrieverService = dataRetrieverService;
        this.userRepository = userRepository;
        this.viewHistoryRepository = viewHistoryRepository;
        this.viewHistoryService = viewHistoryService;
        this.productRepository = productRepository;
    }

    private void setStrategy(User user) {
        int totalProducts = productRepository.findAll().size();
        int totalUniqueProductsViewedByCurrentUser = viewHistoryService.getViewHistoryByUser(user).size();

        // Adjust thresholds based on catalog size
        int basicThreshold = Math.min(10, totalProducts / 10); // Ensure threshold doesn't exceed 10% of catalog
        int advancedThreshold = Math.min(30, totalProducts / 8); // Ensure threshold doesn't exceed 25% of catalog

        if (totalProducts <= 10 || totalUniqueProductsViewedByCurrentUser < basicThreshold) {
            currentStrategy = new SimpleRecommendationStrategy(this.productRepository);
        } else if (totalUniqueProductsViewedByCurrentUser >= basicThreshold && totalUniqueProductsViewedByCurrentUser < advancedThreshold) {
            PythonScriptInvoker pythonInvoker = new PythonScriptInvoker();
            currentStrategy = new ContentBasedRecommendationStrategy(this.productRepository, user, this.viewHistoryRepository, pythonInvoker);
        } else {
            PythonScriptInvoker pythonInvoker = new PythonScriptInvoker();
            currentStrategy = new CollaborativeFilteringStrategy(this.productRepository, user, this.viewHistoryRepository, pythonInvoker);
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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        this.setStrategy(user);
        List<Product> recommendations = currentStrategy.recommend(user);
        return ResponseEntity.ok(recommendations);
    }
}
