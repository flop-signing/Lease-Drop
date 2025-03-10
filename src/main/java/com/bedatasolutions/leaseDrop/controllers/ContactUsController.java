package com.bedatasolutions.leaseDrop.controllers;
import com.bedatasolutions.leaseDrop.dto.ContactUsDto;
import com.bedatasolutions.leaseDrop.services.ContactUsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contact-us") // Base URL for contact inquiries
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "13. Contact Us Controller", description = "Manage Contact Us Records")
public class ContactUsController {

    private final ContactUsService contactUsService;

    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }

    // Endpoint to get all contact inquiries
    @GetMapping
    public ResponseEntity<List<ContactUsDto>> getAllContacts() {
        List<ContactUsDto> contacts = contactUsService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    // Endpoint to get a contact inquiry by ID
    @GetMapping("/{id}")
    public ResponseEntity<ContactUsDto> getContactById(@PathVariable Integer id) {
        Optional<ContactUsDto> contact = contactUsService.getContactById(id);
        return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new contact inquiry
    @PostMapping
    public ResponseEntity<ContactUsDto> create(@RequestBody ContactUsDto contactUsDto) {
        ContactUsDto createdContact = contactUsService.create(contactUsDto);
        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    // Endpoint to update an existing contact inquiry
    @PutMapping
    public ResponseEntity<ContactUsDto> update(@RequestBody ContactUsDto contactUsDto) {
        try {
            ContactUsDto updatedContact = contactUsService.update(contactUsDto);
            return new ResponseEntity<>(updatedContact, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a contact inquiry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            contactUsService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
