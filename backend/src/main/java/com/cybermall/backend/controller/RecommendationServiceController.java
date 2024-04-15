package com.cybermall.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cybermall.backend.model.*;
import com.cybermall.backend.service.DataRetrieverService;
import com.cybermall.backend.service.RecommendationService;
import com.cybermall.backend.repository.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationServiceController {

    private final DataRetrieverService dataRetrieverService;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public RecommendationServiceController(DataRetrieverService dataRetrieverService, RecommendationService recommendationService, UserRepository userRepository) {
        this.dataRetrieverService = dataRetrieverService;
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public List<Product> getRecommendations(@PathVariable Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        
        // Assuming retrieveDataForRecommendation only needs a User object and retrieves the rest
        RecommendationData data = dataRetrieverService.retrieveDataForRecommendation(user);

        boolean isNewUser = user.getIsNewUser();
        System.out.println(isNewUser ? "User is new" : "User is not new");
        System.out.println("Retrieved data for user: " + data.getUser().getUserName());
        List<Product> recommendations = isNewUser ?
            this.recommendationService.recommendUsingSimpleStrategy() :
            this.recommendationService.recommendUsingMLStrategy();

        return recommendations;
    }
}
