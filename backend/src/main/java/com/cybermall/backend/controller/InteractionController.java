package com.cybermall.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.service.InteractionService;

import jakarta.validation.Valid;

/**
 * The controller class that handles interactions related to products.
 */
@RestController
@RequestMapping("/interactions")
public class InteractionController {

    private final InteractionService interactionService;

    /**
     * Constructs a new InteractionController with the specified InteractionService.
     *
     * @param interactionService the InteractionService to be used
     */
    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    /**
     * Simulates a product view interaction.
     *
     * @param request the InteractionRequest containing the user and product information
     * @return the ResponseEntity containing the product information
     */
    @PostMapping("/view")
    public ResponseEntity<Product> simulateView(@Valid @RequestBody InteractionRequest request) {
        Product product = interactionService.simulateProductView(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(product);
    }
}

