package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.UserDto;
import com.bedatasolutions.leaseDrop.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();  // Get all users from the service
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);  // Create user
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);  // Return the created user with HTTP 201
    }

    // Endpoint to update an existing user
    @PutMapping()
    public ResponseEntity<UserDto> updateUser( @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.update(userDto);  // Update user
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if user not found
        }
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            userService.delete(id);  // Delete user
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return 204 (No Content)
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if user not found
        }
    }
}
