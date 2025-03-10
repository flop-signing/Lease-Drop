package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.DropdownTypeDto;
import com.bedatasolutions.leaseDrop.services.DropdownTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropdown-types")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "8. Dropdown Type Controller", description = "Manage Dropdown Type Records")
public class DropdownTypeController {


    private final DropdownTypeService dropdownTypeService;

    public DropdownTypeController(DropdownTypeService dropdownTypeService)
    {
        this.dropdownTypeService=dropdownTypeService;
    }

    // Get All Dropdown Types
    @GetMapping
    public ResponseEntity<List<DropdownTypeDto>> getAllDropdownTypes() {
        List<DropdownTypeDto> dropdownTypes = dropdownTypeService.getAllDropdownTypes();
        return ResponseEntity.ok(dropdownTypes);
    }

    // Get Dropdown Type by ID
    @GetMapping("/{id}")
    public ResponseEntity<DropdownTypeDto> getDropdownTypeById(@PathVariable Integer id) {
        DropdownTypeDto dropdownTypeDto = dropdownTypeService.getDropdownTypeById(id)
                .orElseThrow(() -> new RuntimeException("Dropdown Type not found"));
        return ResponseEntity.ok(dropdownTypeDto);
    }

    // Create Dropdown Type
    @PostMapping
    public ResponseEntity<DropdownTypeDto> create(@RequestBody DropdownTypeDto dropdownTypeDto) {
        DropdownTypeDto createdDropdownType = dropdownTypeService.create(dropdownTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDropdownType);
    }

    // Update Dropdown Type

    @PutMapping()
    public ResponseEntity<DropdownTypeDto> update(@RequestBody @Valid DropdownTypeDto dropdownTypeDto) {
        DropdownTypeDto updatedDropdownType = dropdownTypeService.update(dropdownTypeDto);
        return ResponseEntity.ok(updatedDropdownType);
    }

    // Delete Dropdown Type
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        dropdownTypeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Dropdown Type deleted successfully");
    }
}
