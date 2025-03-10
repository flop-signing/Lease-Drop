package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.SoftwareConfigurationDto;
import com.bedatasolutions.leaseDrop.services.SoftwareConfigurationService;
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
@RequestMapping("/api/software-configurations")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "10. Software Configuration Controller", description = "Manage Software Configuration Records")
public class SoftwareConfigurationController {


    private final SoftwareConfigurationService softwareConfigurationService;

    public SoftwareConfigurationController(SoftwareConfigurationService softwareConfigurationService)
    {
        this.softwareConfigurationService=softwareConfigurationService;
    }

    // Get All Software Configurations
    @GetMapping
    public ResponseEntity<List<SoftwareConfigurationDto>> getAllSoftwareConfigurations() {
        List<SoftwareConfigurationDto> configurations = softwareConfigurationService.getAllSoftwareConfigurations();
        return ResponseEntity.ok(configurations);
    }

    // Get Software Configuration by ID
    @GetMapping("/{id}")
    public ResponseEntity<SoftwareConfigurationDto> getSoftwareConfigurationById(@PathVariable Integer id) {
        SoftwareConfigurationDto configurationDto = softwareConfigurationService.getSoftwareConfigurationById(id)
                .orElseThrow(() -> new RuntimeException("Software Configuration not found"));
        return ResponseEntity.ok(configurationDto);
    }

    // Create Software Configuration

    @PostMapping
    public ResponseEntity<SoftwareConfigurationDto> create(@RequestBody @Valid SoftwareConfigurationDto softwareConfigurationDto) {
        SoftwareConfigurationDto createdConfiguration = softwareConfigurationService.create(softwareConfigurationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConfiguration);
    }

    // Update Software Configuration

    @PutMapping()
    public ResponseEntity<SoftwareConfigurationDto> update( @RequestBody @Valid SoftwareConfigurationDto softwareConfigurationDto) {
        SoftwareConfigurationDto updatedConfiguration = softwareConfigurationService.update( softwareConfigurationDto);
        return ResponseEntity.ok(updatedConfiguration);
    }

    // Delete Software Configuration

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        softwareConfigurationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Software Configuration deleted successfully");
    }
}
