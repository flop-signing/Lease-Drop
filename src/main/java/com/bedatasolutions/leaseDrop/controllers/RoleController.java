package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.RoleDto;
import com.bedatasolutions.leaseDrop.services.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "2. Role Controller", description = "View Interior images")
public class RoleController {

    @Autowired
    private RoleService roleService;



    // Get All Roles
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Get Role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Integer id) {
        RoleDto roleDto = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDto);
    }
    // Create Role
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.create(roleDto);
        return ResponseEntity.ok(createdRole);
    }
    // Update Role
    @PutMapping
    public ResponseEntity<RoleDto> updateRole(@RequestBody @Valid RoleDto roleDto) {
        // Call service to update the role
        System.out.println("id check "+roleDto.id());
        RoleDto updatedRole = roleService.update(roleDto);
        return ResponseEntity.ok(updatedRole);  // Return the updated RoleDto
    }
    // Delete Role
    @DeleteMapping
    public ResponseEntity<String> deleteRole(@RequestBody Integer roleIdDto) {
        roleService.delete(roleIdDto);  // Call the service to delete the role
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Role deleted successfully");
    }

    // Get Roles with Pagination
    @GetMapping("/page")
    public ResponseEntity<List<RoleDto>> getRolesWithPagination(
            @RequestParam int page,
            @RequestParam int size) {
        Page<RoleDto> rolesPage = roleService.getRolesWithPagination(page, size);
        return ResponseEntity.ok(rolesPage.getContent());  // Return the paginated list of roles
    }

}
