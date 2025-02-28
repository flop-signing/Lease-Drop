package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.AnalyticsDto;
import com.bedatasolutions.leaseDrop.services.AnalyticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "4. Analytics Controller", description = "Manage Analytics Records")
public class AnalyticsController {


    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService)
    {
        this.analyticsService=analyticsService;
    }

    // Get All Analytics
    @GetMapping
    public ResponseEntity<List<AnalyticsDto>> getAllAnalytics() {
        List<AnalyticsDto> analytics = analyticsService.getAllAnalytics();
        return ResponseEntity.ok(analytics);
    }

    // Get Analytics by ID
    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsDto> getAnalyticsById(@PathVariable Integer id) {
        AnalyticsDto analyticsDto = analyticsService.getAnalyticsById(id)
                .orElseThrow(() -> new RuntimeException("Analytics not found"));
        return ResponseEntity.ok(analyticsDto);
    }

    // Create Analytics
    @PostMapping
    public ResponseEntity<AnalyticsDto> create(@RequestBody AnalyticsDto analyticsDto) {
        AnalyticsDto createdAnalytics = analyticsService.create(analyticsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnalytics);
    }

    // Update Analytics
    @PutMapping()
    public ResponseEntity<AnalyticsDto> update(@RequestBody @Valid AnalyticsDto analyticsDto) {
        AnalyticsDto updatedAnalytics = analyticsService.update(analyticsDto);
        return ResponseEntity.ok(updatedAnalytics);
    }

    // Delete Analytics
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        analyticsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Analytics record deleted successfully");
    }
}
