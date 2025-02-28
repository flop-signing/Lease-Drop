package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.NotificationDto;
import com.bedatasolutions.leaseDrop.services.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "9. Notification Controller", description = "Manage Notification Records")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Get All Notifications
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Get Notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Integer id) {
        NotificationDto notificationDto = notificationService.getNotificationById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return ResponseEntity.ok(notificationDto);
    }

    // Create Notification

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody @Valid NotificationDto notificationDto) {
        NotificationDto createdNotification = notificationService.create(notificationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    // Update Notification

    @PutMapping()
    public ResponseEntity<NotificationDto> updateNotification( @RequestBody @Valid NotificationDto notificationDto) {
        NotificationDto updatedNotification = notificationService.update(notificationDto);
        return ResponseEntity.ok(updatedNotification);
    }

    // Delete Notification
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id) {
        notificationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Notification deleted successfully");
    }
}
