package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dao.ClassifierDao;
import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import com.bedatasolutions.leaseDrop.dto.ClassifierDto;
import com.bedatasolutions.leaseDrop.dto.CustomerDto;
import com.bedatasolutions.leaseDrop.dto.rest.RestPageResponse;
import com.bedatasolutions.leaseDrop.dto.rest.RestQuery;
import com.bedatasolutions.leaseDrop.services.ClassifierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classifiers")
public class ClassifierController {

    private final ClassifierService classifierService;

    public ClassifierController(ClassifierService classifierService) {
        this.classifierService = classifierService;
    }

    // Create - Add a new Classifier
    @PostMapping
    public ResponseEntity<ClassifierDto> createClassifier(@RequestBody ClassifierDto classifierDto) {
        ClassifierDto createdClassifier = classifierService.create(classifierDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(createdClassifier);
    }



/*
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllClassifiers(
            @RequestParam( required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String direction) {

        // Call the service to get the paginated and sorted classifiers
        Map<String, Object> response = classifierService.getAllClassifiers(page, size, field, direction);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

*/


    @GetMapping( consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestPageResponse<ClassifierDao, ClassifierDto> getAllClassifier(
            @RequestBody(required = false) RestQuery query) {

        // Call the service method to fetch customers with pagination, sorting, and filtering
//        return customerService.getAllCustomers(page, size, new RestSort(field, direction), filters);
//        return customerService.getAllCustomers(query.page().pageNumber(), query.page().size(), query.sort(), query.filter().filters());
        return classifierService.getAllClassifiers(query.page(), query.sort(), query.filter());
    }

    // Read - Get a Classifier by ID
    @GetMapping("/{id}")
    public ResponseEntity<ClassifierDto> getClassifierById(@PathVariable Integer id) {
        ClassifierDto classifierDto = classifierService.getClassifierById(id);
        if (classifierDto != null) {
            ResponseEntity.ok(classifierDto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(classifierDto, HttpStatus.OK);
    }




    // Update - Update an existing Classifier
    @PutMapping()
    public ResponseEntity<ClassifierDto> update( @RequestBody @Valid ClassifierDto classifierDto) {
        ClassifierDto updatedClassifier = classifierService.update(classifierDto);
        return ResponseEntity.ok(updatedClassifier);
    }

    // Delete - Delete a Classifier by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = classifierService.delete(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }



    }
}
