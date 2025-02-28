package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.DocumentDao;
import com.bedatasolutions.leaseDrop.dto.DocumentDto;
import com.bedatasolutions.leaseDrop.repo.DocumentRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepo documentRepo;

    // Constructor-based dependency injection for the repository
    public DocumentService(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }


    // Method to get all documents
    public List<DocumentDto> getAllDocuments() {
        List<DocumentDao> documentDaos = documentRepo.findAll(); // Fetch all documents from the database
        return documentDaos.stream()
                .map(this::daoToDto) // Convert each DocumentDao to DocumentDto
                .collect(Collectors.toList());
    }


    // Method to get a single document by its ID
    public Optional<DocumentDto> getDocumentById(Integer id) {
        DocumentDao documentDao = documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return Optional.of(daoToDto(documentDao)); // Convert and return as DTO
    }


    @Transactional
    // Method to create a new document
    public DocumentDto create(DocumentDto documentDto) {
        // Convert DTO to DAO
        DocumentDao documentDao = dtoToDao(documentDto,new DocumentDao());

        documentDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        DocumentDao savedDocument = documentRepo.save(documentDao);
        return daoToDto(savedDocument); // Return the created document as DTO
    }


    @Transactional
    // Method to update an existing document
    public DocumentDto update( DocumentDto documentDto) {
        DocumentDao existingDocument = documentRepo.getReferenceById(documentDto.id());

        if (existingDocument.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found with id: " + documentDto.id());
        }

        DocumentDao updatedDocument=documentRepo.save(dtoToDao(documentDto,existingDocument));
        return daoToDto(updatedDocument);
    }



    @Transactional
    // Method to delete a document by ID
    public void delete(Integer id) {
        DocumentDao documentDao = documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        documentRepo.delete(documentDao); // Delete the document
    }


    public DocumentDto daoToDto(DocumentDao documentDao) {
        return new DocumentDto(
                documentDao.getId(),
                documentDao.getVersion(),
                documentDao.getFilePath()
        );
    }

    public DocumentDao dtoToDao(DocumentDto documentDto, DocumentDao documentDao) {

        documentDao.setId(documentDto.id());
        documentDao.setVersion(documentDto.version());
        documentDao.setFilePath(documentDto.filePath());
        return documentDao;
    }


}
