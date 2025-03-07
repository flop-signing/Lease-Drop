package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.DocumentDao;
import com.bedatasolutions.leaseDrop.dao.DocumentSummaryDao;
import com.bedatasolutions.leaseDrop.dto.DocumentSummaryDto;
import com.bedatasolutions.leaseDrop.repo.DocumentRepo;
import com.bedatasolutions.leaseDrop.repo.DocumentSummaryRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.Document;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentSummaryService {

    private final DocumentSummaryRepo documentSummaryRepo;
    private final DocumentRepo documentRepo;

    // Constructor-based dependency injection for the repository
    public DocumentSummaryService(DocumentSummaryRepo documentSummaryRepo,DocumentRepo documentRepo) {
        this.documentSummaryRepo = documentSummaryRepo;
        this.documentRepo = documentRepo;
    }

    // Method to get all document summaries
    public List<DocumentSummaryDto> getAllDocumentSummaries() {
        List<DocumentSummaryDao> documentSummaryDaos = documentSummaryRepo.findAll(); // Fetch all document summaries from the database
        return documentSummaryDaos.stream()
                .map(this::daoToDto) // Convert each DocumentSummaryDao to DocumentSummaryDto
                .collect(Collectors.toList());
    }

    // Method to get a single document summary by its ID
    public Optional<DocumentSummaryDto> getDocumentSummaryById(Integer id) {
        DocumentSummaryDao documentSummaryDao = documentSummaryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document Summary not found"));
        return Optional.of(daoToDto(documentSummaryDao)); // Convert and return as DTO
    }


    @Transactional
    // Method to create a new document summary
    public DocumentSummaryDto create(DocumentSummaryDto documentSummaryDto) {
        // Convert DTO to DAO
        DocumentSummaryDao documentSummaryDao = dtoToDao(documentSummaryDto,new DocumentSummaryDao());

        // Save the entity to the database
        documentSummaryDao.setActionKey(ActionType.CREATE);
        DocumentSummaryDao savedDocumentSummary = documentSummaryRepo.save(documentSummaryDao);
        return daoToDto(savedDocumentSummary); // Return the created document summary as DTO
    }

    @Transactional
    // Method to update an existing document summary
    public DocumentSummaryDto update(DocumentSummaryDto documentSummaryDto) {
        DocumentSummaryDao existingDocumentSummary = documentSummaryRepo.getReferenceById(documentSummaryDto.id());

        if (existingDocumentSummary.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document Summary not found with id: " + documentSummaryDto.id());
        }

        existingDocumentSummary.setActionKey(ActionType.UPDATE);

        DocumentSummaryDao updatedDocumentSummary=documentSummaryRepo.save(dtoToDao(documentSummaryDto,existingDocumentSummary));
        return daoToDto(updatedDocumentSummary);

    }


    @Transactional
    // Method to delete a document summary by ID
    public void delete(Integer id) {
        DocumentSummaryDao documentSummaryDao = documentSummaryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document Summary not found"));

        documentSummaryDao.setActionKey(ActionType.DELETE);
        documentSummaryRepo.delete(documentSummaryDao); // Delete the document summary
    }



    public DocumentSummaryDto daoToDto(DocumentSummaryDao documentSummaryDao) {
        return new DocumentSummaryDto(
                documentSummaryDao.getId(),
                documentSummaryDao.getVersion(),
                documentSummaryDao.getSummary(),
                documentSummaryDao.getMetaData(),
                documentSummaryDao.getDocuments().getId()
        );
    }

    public DocumentSummaryDao dtoToDao(DocumentSummaryDto documentSummaryDto, DocumentSummaryDao documentSummaryDao) {

        documentSummaryDao.setId(documentSummaryDto.id());
        documentSummaryDao.setVersion(documentSummaryDto.version());
        documentSummaryDao.setSummary(documentSummaryDto.summary());
        documentSummaryDao.setMetaData(documentSummaryDto.metaData());

        DocumentDao documentDao =documentRepo.findById(documentSummaryDto.documentId()).orElseThrow(() -> new RuntimeException("Document not found with id: " + documentSummaryDto.documentId()));
        documentSummaryDao.setDocuments(documentDao);

        return documentSummaryDao;
    }

}
