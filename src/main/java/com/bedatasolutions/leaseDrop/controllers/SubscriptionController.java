package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.SubscriptionDto;
import com.bedatasolutions.leaseDrop.services.SubscriptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "10. Subscription Controller", description = "Manage Subscription Records")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Get All Subscriptions
    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptions() {
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    // Get Subscription by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Integer id) {
        SubscriptionDto subscriptionDto = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return ResponseEntity.ok(subscriptionDto);
    }

    // Create Subscription
    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody @Valid SubscriptionDto subscriptionDto) {
        SubscriptionDto createdSubscription = subscriptionService.create(subscriptionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
    }

    // Update Subscription
    @PutMapping()
    public ResponseEntity<SubscriptionDto> updateSubscription( @RequestBody @Valid SubscriptionDto subscriptionDto) {
        SubscriptionDto updatedSubscription = subscriptionService.update(subscriptionDto);
        return ResponseEntity.ok(updatedSubscription);
    }

    // Delete Subscription
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Subscription deleted successfully");
    }
}
