package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.DocumentDto;
import com.bedatasolutions.leaseDrop.services.DocumentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "5. Document Controller", description = "Manage Document Records")
public class DocumentController {


    private final DocumentService documentService;

    public DocumentController(DocumentService documentService)
    {
        this.documentService=documentService;
    }

    // Get All Documents
    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        List<DocumentDto> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    // Get Document by ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Integer id) {
        DocumentDto documentDto = documentService.getDocumentById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return ResponseEntity.ok(documentDto);
    }

    // Create Document
    @PostMapping
    public ResponseEntity<DocumentDto> create(@RequestBody DocumentDto documentDto) {
        DocumentDto createdDocument = documentService.create(documentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }

    // Update Document
    @PutMapping()
    public ResponseEntity<DocumentDto> update(@RequestBody @Valid DocumentDto documentDto) {
        DocumentDto updatedDocument = documentService.update(documentDto);
        return ResponseEntity.ok(updatedDocument);
    }

    // Delete Document
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = documentService.delete(id); // Delete document

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
