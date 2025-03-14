package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.ClassifierDao;
import com.bedatasolutions.leaseDrop.dto.ClassifierDto;
import com.bedatasolutions.leaseDrop.repo.ClassifierRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassifierService {

    private final ClassifierRepo classifierRepo;
    public ClassifierService(ClassifierRepo classifierRepo) {
        this.classifierRepo = classifierRepo;
    }
    // Create Classifier
    @Transactional
    public ClassifierDto create(ClassifierDto classifierDto) {
        // Convert DTO to DAO (ClassifierDao)
        ClassifierDao classifierDao = dtoToDao(classifierDto, new ClassifierDao());
        classifierDao.setActionKey(ActionType.CREATE);

        // Save the classifier and return the saved DTO
        ClassifierDao savedClassifier = classifierRepo.save(classifierDao);
        return daoToDto(savedClassifier);
    }

    // Get a single Classifier by ID
    public ClassifierDto getClassifierById(Integer id) {
        ClassifierDao classifierDao = classifierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Classifier not found with id: " + id));
        return daoToDto(classifierDao);
    }

    // Update Existing Classifier
    @Transactional
    public ClassifierDto update(ClassifierDto classifierDto) {
        // Find existing classifier by ID
        ClassifierDao existingClassifier = classifierRepo.getReferenceById(classifierDto.id());
        if (existingClassifier.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Classifier not found with id: " + classifierDto.id());
        }

        // Save the updated classifier
        existingClassifier.setActionKey(ActionType.UPDATE);
        ClassifierDao updatedClassifier = classifierRepo.save(dtoToDao(classifierDto, existingClassifier));

        // Return the updated ClassifierDto
        return daoToDto(updatedClassifier);
    }

    // Delete Classifier
    @Transactional
    public boolean delete(Integer id) {
        ClassifierDao classifierDao = classifierRepo.findById(id).orElse(null);
        if (classifierDao == null) {
            return false;
        }

        classifierDao.setActionKey(ActionType.DELETE);
        classifierRepo.delete(classifierDao);
        return true;
    }

    // Convert DAO to DTO
    public ClassifierDto daoToDto(ClassifierDao classifierDao) {
        List<String> relationList = ClassifierDto.convertCsvToList(classifierDao.getRelation());
        return new ClassifierDto(
                classifierDao.getId(),
                classifierDao.getVersion(),
                classifierDao.getName(),
                classifierDao.getGroupKey(),
                classifierDao.getDescription(),
                classifierDao.getType(),
                classifierDao.isActive(),
                relationList
        );
    }

    // Convert DTO to DAO
    public ClassifierDao dtoToDao(ClassifierDto classifierDto, ClassifierDao classifierDao) {
        classifierDao.setName(classifierDto.name());
        classifierDao.setVersion(classifierDto.version());
        classifierDao.setDescription(classifierDto.description());
        classifierDao.setGroupKey(classifierDto.groupKey());
        classifierDao.setType(classifierDto.type());
        classifierDao.setActive(classifierDto.isActive());

        // Convert List<String> to CSV string and set it
        classifierDao.setRelation(String.join(",", classifierDto.relation()));

        return classifierDao;
    }




    // Method for getting all classifiers with pagination and sorting
    public Map<String, Object> getAllClassifiers(Integer page, Integer size, String field, String direction) {
        // Set default values if null
        if (page == null || page < 1) page = 1; // Default to page 1
        if (size == null || size <= 0) size = 10; // Default to size 10
        if (field == null || field.trim().isEmpty()) field = "id"; // Default to `id`
        if (direction == null || direction.trim().isEmpty()) direction = "desc"; // Default to descending

        // Skip sorting by 'id_classifier_key' (or any other field you wish to exclude)
        if ("id_classifier_key".equalsIgnoreCase(field)) {
            field = "id";  // Default to `id` if sorting by `id_classifier_key`
        }

        // Define sorting direction
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortDirection, field));

        // Fetch the classifiers with pagination and sorting
        Page<ClassifierDao> classifierPage = classifierRepo.findAll(pageRequest);

        // Convert the ClassifierDao to ClassifierDto
        List<ClassifierDto> classifierDtos = classifierPage.getContent().stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());

        // Prepare the response with pagination details
        Map<String, Object> response = new HashMap<>();
        response.put("payload", classifierDtos);

        Map<String, Object> header = new HashMap<>();
        header.put("pageNo", page);
        header.put("totalPages", classifierPage.getTotalPages());
        header.put("pageSize", size);
        header.put("totalElements", classifierPage.getTotalElements());

        response.put("header", header);

        return response;
    }



}
