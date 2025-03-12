package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.CustomerDto;
import com.bedatasolutions.leaseDrop.services.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> create(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerService.create(customerDto);
        return ResponseEntity.ok(createdCustomer);
    }

    // Get All Customers
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
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
        System.out.println("id check " + customerDto.id());
        CustomerDto updatedCustomer = customerService.update(customerDto);
        return ResponseEntity.ok(updatedCustomer);  // Return the updated CustomerDto
    }

    // Delete Customer
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody Integer customerIdDto) {
        customerService.delete(customerIdDto);  // Call the service to delete the customer
        return ResponseEntity.ok("Customer deleted successfully");
    }
}
