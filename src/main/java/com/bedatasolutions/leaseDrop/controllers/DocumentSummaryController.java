package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.DocumentSummaryDto;
import com.bedatasolutions.leaseDrop.services.DocumentSummaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-summaries")
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "6. Document Summary Controller", description = "Manage Document Summary Records")
public class DocumentSummaryController {


    private final DocumentSummaryService documentSummaryService;

    public DocumentSummaryController(DocumentSummaryService documentSummaryService)
    {
        this.documentSummaryService=documentSummaryService;
    }

    // Get All Document Summaries
    @GetMapping
    public ResponseEntity<List<DocumentSummaryDto>> getAllDocumentSummaries() {
        List<DocumentSummaryDto> documentSummaries = documentSummaryService.getAllDocumentSummaries();
        return ResponseEntity.ok(documentSummaries);
    }

    // Get Document Summary by ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentSummaryDto> getDocumentSummaryById(@PathVariable Integer id) {
        DocumentSummaryDto documentSummaryDto = documentSummaryService.getDocumentSummaryById(id)
                .orElseThrow(() -> new RuntimeException("Document Summary not found"));
        return ResponseEntity.ok(documentSummaryDto);
    }

    // Create Document Summary
    @PostMapping
    public ResponseEntity<DocumentSummaryDto> create(@RequestBody DocumentSummaryDto documentSummaryDto) {
        DocumentSummaryDto createdDocumentSummary = documentSummaryService.create(documentSummaryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocumentSummary);
    }

    // Update Document Summary
    @PutMapping()
    public ResponseEntity<DocumentSummaryDto> update( @RequestBody @Valid DocumentSummaryDto documentSummaryDto) {
        DocumentSummaryDto updatedDocumentSummary = documentSummaryService.update(documentSummaryDto);
        return ResponseEntity.ok(updatedDocumentSummary);
    }

    // Delete Document Summary
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = documentSummaryService.delete(id); // Delete document summary

        if (isDeleted) {
            // Return HTTP 202 Accepted (with no message body)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 204 No Content (with no message body)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
