package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.SubscriptionPlanDto;
import com.bedatasolutions.leaseDrop.services.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "11. Subscription Plan Controller", description = "Manage Subscription Plan Records")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    // Get All Subscription Plans
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDto>> getAllSubscriptionPlans() {
        List<SubscriptionPlanDto> subscriptionPlans = subscriptionPlanService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptionPlans);
    }

    // Get Subscription Plan by Name
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDto> getSubscriptionPlanById(@PathVariable Integer id) {
        return subscriptionPlanService.getSubscriptionPlanById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Subscription Plan not found with name: " + id));
    }

    // Create Subscription Plan
    @PostMapping()
    public ResponseEntity<SubscriptionPlanDto> create(@RequestBody @Valid SubscriptionPlanDto subscriptionPlanDto) {
        SubscriptionPlanDto createdSubscriptionPlan = subscriptionPlanService.create(subscriptionPlanDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscriptionPlan);
    }

    // Update Subscription Plan
    @PutMapping()
    public ResponseEntity<SubscriptionPlanDto> update(@RequestBody @Valid SubscriptionPlanDto subscriptionPlanDto) {
        SubscriptionPlanDto updatedSubscriptionPlan = subscriptionPlanService.update(subscriptionPlanDto);
        return ResponseEntity.ok(updatedSubscriptionPlan);
    }

    // Delete Subscription Plan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = subscriptionPlanService.delete(id); // Delete subscription plan

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
