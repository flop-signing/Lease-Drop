package com.bedatasolutions.leaseDrop.controllers;


import com.bedatasolutions.leaseDrop.dto.PermissionDto;
import com.bedatasolutions.leaseDrop.services.PermissonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "1. Permission Module API Documentation", description = "The Permission Module in LeaseDrop is responsible for managing user permissions related to lease document analysis. ")
public class PermissionController {

    private final PermissonService permissonService;

    public PermissionController(PermissonService permissonService) {
        this.permissonService = permissonService;
    }

    // Create Permission
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PermissionDto permissionDto) {
        PermissionDto createdPermission = permissonService.create(permissionDto);
        return ResponseEntity.ok(createdPermission);
    }

    // Get All Permissions
    @GetMapping
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissonService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    // Get Permission by ID
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Integer id) {
        PermissionDto permissionDto = permissonService.getPermissionById(id);
        return ResponseEntity.ok(permissionDto);
    }

    // Update Role
    @PutMapping
    public ResponseEntity<PermissionDto> update(@RequestBody @Valid PermissionDto permissionDto) {
        // Call service to update the role
        System.out.println("id check " + permissionDto.id());
        PermissionDto updatedRole = permissonService.update(permissionDto);
        return ResponseEntity.ok(updatedRole);  // Return the updated RoleDto
    }

    // Delete Role
// Delete Permission
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Integer permissionIdDto) {
        boolean isDeleted = permissonService.delete(permissionIdDto); // Delete permission

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


}
