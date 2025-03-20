package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dao.UserDao;
import com.bedatasolutions.leaseDrop.dto.UserDto;
import com.bedatasolutions.leaseDrop.dto.rest.RestPageResponse;
import com.bedatasolutions.leaseDrop.dto.rest.RestQuery;
import com.bedatasolutions.leaseDrop.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Base URL for user-related API endpoints
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "12. User Controller", description = "Manage User Records")
public class UserController {


    private final UserService userService;  // Inject the UserService to access business logic

    public UserController(UserService userService)
    {
        this.userService=userService;
    }

    // Endpoint to get all users

    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestPageResponse<UserDao, UserDto> getAllUsers(
            @RequestBody(required = false) RestQuery query) {

        // Call the service method to fetch customers with pagination, sorting, and filtering
    //        return customerService.getAllCustomers(page, size, new RestSort(field, direction), filters);
    //        return customerService.getAllCustomers(query.page().pageNumber(), query.page().size(), query.sort(), query.filter().filters());
        return userService.getAllUsers(query.page(), query.sort(), query.filter());
    }


    // Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }



    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);  // Create user
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);  // Return the created user with HTTP 201
    }

    // Update Customer
    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDto) {
        // Call service to update the customer

        UserDto updatedCustomer = userService.update(userDto);
        return ResponseEntity.ok(updatedCustomer);  // Return the updated CustomerDto
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = userService.delete(id);

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}



