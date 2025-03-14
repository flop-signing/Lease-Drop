package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.CustomerDto;
import com.bedatasolutions.leaseDrop.services.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


/*    // Get All Customers
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }*/


/*    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String direction) {

        Map<String, Object> response = customerService.getAllCustomers(page, size, field, direction);

        return ResponseEntity.ok(response);  // Return the response with HTTP 200 OK status
    }*/


    @GetMapping("/all")
    public Map<String, Object> getCustomers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String packageType,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) LocalDate purchaseDate,
            @RequestParam(required = false) LocalDate expireDate,
            @RequestParam(required = false) Integer remainingDays,
            @RequestParam(required = false) Integer fileProcessing) {

        // Prepare the filters map by passing all optional parameters
        Map<String, Object> filterParams = Map.of(
                "name", name,
                "packageType", packageType,
                "amount", amount,
                "purchaseDate", purchaseDate,
                "expireDate", expireDate,
                "remainingDays", remainingDays,
                "fileProcessing", fileProcessing
        );

        // Call the service method to get customers with dynamic filtering
        return customerService.getAllCustomers(page, size, field, direction, filterParams);
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
