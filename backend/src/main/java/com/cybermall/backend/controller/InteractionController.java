package com.cybermall.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cybermall.backend.model.*;
import com.cybermall.backend.service.InteractionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/interactions")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/view")
    public ResponseEntity<Product> simulateView(@Valid @RequestBody InteractionRequest request) {
        Product product = interactionService.simulateProductView(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> simulateOrder(@Valid @RequestBody InteractionRequest request) {
        Order order = interactionService.simulateProductOrder(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(order);
    }
}

