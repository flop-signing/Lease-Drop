package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.DropdownItemDto;
import com.bedatasolutions.leaseDrop.services.DropdownItemService;
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
@RequestMapping("/api/dropdown-items")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "7. Dropdown Item Controller", description = "Manage Dropdown Item Records")
public class DropdownItemController {

    @Autowired
    private final DropdownItemService dropdownItemService;

    public DropdownItemController(DropdownItemService dropdownItemService)
    {
        this.dropdownItemService=dropdownItemService;
    }

    // Get All Dropdown Items
    @GetMapping
    public ResponseEntity<List<DropdownItemDto>> getAllDropdownItems() {
        List<DropdownItemDto> dropdownItems = dropdownItemService.getAllDropdownItems();
        return ResponseEntity.ok(dropdownItems);
    }

    // Get Dropdown Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<DropdownItemDto> getDropdownItemById(@PathVariable Integer id) {
        DropdownItemDto dropdownItemDto = dropdownItemService.getDropdownItemById(id)
                .orElseThrow(() -> new RuntimeException("Dropdown Item not found"));
        return ResponseEntity.ok(dropdownItemDto);
    }

    // Create Dropdown Item
    @PostMapping()
    public ResponseEntity<DropdownItemDto> createDropdownItem(@RequestBody DropdownItemDto dropdownItemDto) {
        DropdownItemDto createdDropdownItem = dropdownItemService.create(dropdownItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDropdownItem);
    }

    // Update Dropdown Item
    @PutMapping()
    public ResponseEntity<DropdownItemDto> updateDropdownItem( @RequestBody @Valid DropdownItemDto dropdownItemDto) {
        DropdownItemDto updatedDropdownItem = dropdownItemService.update(dropdownItemDto);
        return ResponseEntity.ok(updatedDropdownItem);
    }

    // Delete Dropdown Item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDropdownItem(@PathVariable Integer id) {
        dropdownItemService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Dropdown Item deleted successfully");
    }
}
