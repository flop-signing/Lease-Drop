package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.SubscriptionPlanDetailDto;
import com.bedatasolutions.leaseDrop.services.SubscriptionPlanDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plan-details")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "12. Subscription Plan Detail Controller", description = "Manage Subscription Plan Detail Records")
public class SubscriptionPlanDetailController {

    private final SubscriptionPlanDetailService subscriptionPlanDetailService;

    public SubscriptionPlanDetailController(SubscriptionPlanDetailService subscriptionPlanDetailService) {
        this.subscriptionPlanDetailService = subscriptionPlanDetailService;
    }

    // Get All Subscription Plan Details
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDetailDto>> getAllSubscriptionPlanDetails() {
        List<SubscriptionPlanDetailDto> subscriptionPlanDetails = subscriptionPlanDetailService.getAllSubscriptionPlanDetails();
        return ResponseEntity.ok(subscriptionPlanDetails);
    }

    // Get Subscription Plan Detail by Name
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDetailDto> getSubscriptionPlanDetailById(@PathVariable Integer id) {
        return subscriptionPlanDetailService.getSubscriptionPlanByDetailId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Subscription Plan Detail not found with name: " + id));
    }

    // Create Subscription Plan Detail
    @PostMapping
    public ResponseEntity<SubscriptionPlanDetailDto> create(@RequestBody @Valid SubscriptionPlanDetailDto subscriptionPlanDetailDto) {
        SubscriptionPlanDetailDto createdSubscriptionPlanDetail = subscriptionPlanDetailService.create(subscriptionPlanDetailDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscriptionPlanDetail);
    }

    // Update Subscription Plan Detail
    @PutMapping()
    public ResponseEntity<SubscriptionPlanDetailDto> update(@RequestBody @Valid SubscriptionPlanDetailDto subscriptionPlanDetailDto) {
        SubscriptionPlanDetailDto updatedSubscriptionPlanDetail = subscriptionPlanDetailService.update(subscriptionPlanDetailDto);
        return ResponseEntity.ok(updatedSubscriptionPlanDetail);
    }

    // Delete Subscription Plan Detail
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        subscriptionPlanDetailService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Subscription Plan Detail deleted successfully: " + id);
    }
}
