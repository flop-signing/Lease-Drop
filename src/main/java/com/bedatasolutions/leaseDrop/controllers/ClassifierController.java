package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.ClassifierDto;
import com.bedatasolutions.leaseDrop.services.ClassifierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    // Read - Get all Classifiers
/*
    @GetMapping
    public ResponseEntity<List<ClassifierDto>> getAllClassifiers() {
        List<ClassifierDto> classifiers = classifierService.getAllClassifiers();
        return new ResponseEntity<>(classifiers, HttpStatus.OK);
    }
*/

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
