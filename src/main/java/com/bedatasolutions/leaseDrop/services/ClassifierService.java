package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.ClassifierDao;
import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import com.bedatasolutions.leaseDrop.dto.ClassifierDto;
import com.bedatasolutions.leaseDrop.dto.rest.RestPage;
import com.bedatasolutions.leaseDrop.dto.rest.RestPageResponse;
import com.bedatasolutions.leaseDrop.dto.rest.RestSort;
import com.bedatasolutions.leaseDrop.repo.ClassifierRepo;
import com.bedatasolutions.leaseDrop.utils.ClassMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassifierService {

    private final ClassifierRepo classifierRepo;

    private final Map<String, Class<?>> COLUMN_TYPE_MAP;

    public ClassifierService(ClassifierRepo classifierRepo) {
        this.classifierRepo = classifierRepo;
        this.COLUMN_TYPE_MAP = ClassMapper.buildColumnTypeMap(ClassifierDao.class);

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



    public RestPageResponse<ClassifierDao, ClassifierDto> getAllClassifiers(RestPage page, RestSort sort,
                                                                      Map<String, String> filters) {
        // Define sorting direction
        Sort sortE = sort.direction().equalsIgnoreCase("asc")
                ? Sort.by(sort.field()).ascending() : Sort.by(sort.field()).descending();

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page.pageNumber()-1, page.size(), sortE);


//        logger.info("Input filters: {}", filters);

        // Convert filter values to their appropriate types dynamically
        Map<String, Object> typedFilters = filters.entrySet().stream()
                .filter(entry ->
                        entry.getValue() != null
                                && !entry.getValue().isEmpty()
                                && COLUMN_TYPE_MAP.containsKey(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ClassMapper.convertValue(entry.getValue(), COLUMN_TYPE_MAP.get(entry.getKey()))
                ));
//        logger.info("Typed filters: {}", typedFilters);


        // Create the dynamic specification using the filters map
        Specification<ClassifierDao> spec = ClassMapper.createSpecification(typedFilters);

        // Fetch the customers with pagination and sorting, applying the filters
        Page<ClassifierDao> classifierPage = classifierRepo.findAll(spec, pageRequest);

        // Convert the CustomerDao to CustomerDto
        List<ClassifierDto> classifierDtos = classifierPage.getContent().stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());
        return new RestPageResponse<>(classifierDtos, classifierPage);
    }



}
