package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.CustomerDto;
import com.bedatasolutions.leaseDrop.services.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "13. Customer Module API Documentation", description = "The Customer Module in LeaseDrop is responsible for managing customer data related to lease document analysis.")
public class CustomerController {


    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create Customer
    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto customerDto) {
        // Create the customer (call service method)
        CustomerDto createdCustomer = customerService.create(customerDto);

        // Return HTTP 201 Created with the created CustomerDto in the response body
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }




    @GetMapping
    public Map<String, Object> getAllCustomers(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam MultiValueMap<String, String> filters) {

        // Call the service method to fetch customers with pagination, sorting, and filtering
        return customerService.getAllCustomers(page, size, field, direction, filters);
    }

    // Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    // Update Customer
    @PutMapping
    public ResponseEntity<CustomerDto> update(@RequestBody @Valid CustomerDto customerDto) {
        // Call service to update the customer

        CustomerDto updatedCustomer = customerService.update(customerDto);
        return ResponseEntity.ok(updatedCustomer);  // Return the updated CustomerDto
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = customerService.delete(id);

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
