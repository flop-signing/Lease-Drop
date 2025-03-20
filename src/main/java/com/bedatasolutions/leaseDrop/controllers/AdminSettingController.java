package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.AdminSettingDto;
import com.bedatasolutions.leaseDrop.services.AdminSettingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-settings")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "3. Admin Setting Controller", description = "Manage Admin Settings")
public class AdminSettingController {


    private final AdminSettingService adminSettingService;

    public AdminSettingController(AdminSettingService adminSettingService)
    {
        this.adminSettingService=adminSettingService;
    }

    // Get All Admin Settings
    @GetMapping
    public ResponseEntity<List<AdminSettingDto>> getAllAdminSettings() {
        List<AdminSettingDto> adminSettings = adminSettingService.getAllAdminSettings();
        return ResponseEntity.ok(adminSettings);
    }

    // Get Admin Setting by ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminSettingDto> getAdminSettingById(@PathVariable Integer id) {
        AdminSettingDto adminSettingDto = adminSettingService.getAdminSettingById(id)
                .orElseThrow(() -> new RuntimeException("Admin setting not found"));
        return ResponseEntity.ok(adminSettingDto);
    }

    // Create Admin Setting
    @PostMapping
    public ResponseEntity<AdminSettingDto> create(@RequestBody AdminSettingDto adminSettingDto) {

        AdminSettingDto createdAdminSetting = adminSettingService.create(adminSettingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdminSetting);
    }

    // Update Admin Setting
    @Transactional
    @PutMapping()
    public ResponseEntity<AdminSettingDto> update(@RequestBody @Valid AdminSettingDto adminSettingDto) {
        System.out.println("id check " + adminSettingDto.id());
        AdminSettingDto updatedAdminSetting = adminSettingService.update(adminSettingDto);
        return ResponseEntity.ok(updatedAdminSetting);
    }


    // Delete Admin Setting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = adminSettingService.delete(id); // Delete admin setting

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
