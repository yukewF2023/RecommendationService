package com.cybermall.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.service.*;
import com.cybermall.backend.repository.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationServiceController {

    private final DataRetrieverService dataRetrieverService;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final ViewHistoryService viewHistoryService;
    private final ProductRepository productRepository;

    public RecommendationServiceController(DataRetrieverService dataRetrieverService,
                                           RecommendationService recommendationService,
                                           UserRepository userRepository,
                                           ViewHistoryService viewHistoryService,
                                           ProductRepository productRepository) {
        this.dataRetrieverService = dataRetrieverService;
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.viewHistoryService = viewHistoryService;
        this.productRepository = productRepository;
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
        int totalProducts = productRepository.findAll().size();
        int totalUniqueProductsViewedByCurrentUser = viewHistoryService.getViewHistoryByUser(user).size();

        List<Product> recommendations;

        // Adjust thresholds based on catalog size
        int basicThreshold = Math.min(10, totalProducts / 10); // Ensure threshold doesn't exceed 10% of catalog
        int advancedThreshold = Math.min(30, totalProducts / 4); // Ensure threshold doesn't exceed 25% of catalog

        if (totalProducts <= 10 || totalUniqueProductsViewedByCurrentUser < basicThreshold) {
            recommendations = recommendationService.recommendUsingSimpleStrategy();
        } else if (totalUniqueProductsViewedByCurrentUser >= basicThreshold && totalUniqueProductsViewedByCurrentUser < advancedThreshold) {
            recommendations = recommendationService.recommendUsingContentBasedStrategy(user);
        } else {
            recommendations = recommendationService.recommendUsingCollaborativeFilteringStrategy(user);
        }

        return ResponseEntity.ok(recommendations);
    }
}
