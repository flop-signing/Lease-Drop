package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.DropdownTypeDao;
import com.bedatasolutions.leaseDrop.dto.DropdownTypeDto;
import com.bedatasolutions.leaseDrop.repo.DropdownTypeRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DropdownTypeService {
    private final DropdownTypeRepo dropdownTypeRepo;

    // Constructor-based dependency injection for the repository
    public DropdownTypeService(DropdownTypeRepo dropdownTypeRepo) {
        this.dropdownTypeRepo = dropdownTypeRepo;
    }

    // Method to get all dropdown types
    public List<DropdownTypeDto> getAllDropdownTypes() {
        List<DropdownTypeDao> dropdownTypeDaos = dropdownTypeRepo.findAll(); // Fetch all dropdown types from the database
        return dropdownTypeDaos.stream()
                .map(this::daoToDto) // Convert each DropdownTypeDao to DropdownTypeDto
                .collect(Collectors.toList());
    }

    // Method to get a single dropdown type by its ID
    public Optional<DropdownTypeDto> getDropdownTypeById(Integer id) {
        DropdownTypeDao dropdownTypeDao = dropdownTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dropdown Type not found"));
        return Optional.of(daoToDto(dropdownTypeDao)); // Convert and return as DTO
    }

    // Method to create a new dropdown type
    @Transactional
    public DropdownTypeDto create(DropdownTypeDto dropdownTypeDto) {
        // Convert DTO to DAO
        DropdownTypeDao dropdownTypeDao = dtoToDao(dropdownTypeDto,new DropdownTypeDao());

        // Save the entity to the database
        dropdownTypeDao.setActionKey(ActionType.CREATE);
        DropdownTypeDao savedDropdownType = dropdownTypeRepo.save(dropdownTypeDao);
        return daoToDto(savedDropdownType); // Return the created dropdown type as DTO
    }

    // Method to update an existing dropdown type
    @Transactional
    public DropdownTypeDto update(DropdownTypeDto dropdownTypeDto) {
        DropdownTypeDao existingDropdownType = dropdownTypeRepo.getReferenceById(dropdownTypeDto.id());

        if (existingDropdownType.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DropDown Type not found with id: " + dropdownTypeDto.id());
        }

        DropdownTypeDao updatedDropdownType=dropdownTypeRepo.save(dtoToDao(dropdownTypeDto,existingDropdownType));
        return daoToDto(updatedDropdownType);
    }

    // Method to delete a dropdown type by ID
    @Transactional
    public void delete(Integer id) {
        DropdownTypeDao dropdownTypeDao = dropdownTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dropdown Type not found"));
        dropdownTypeRepo.delete(dropdownTypeDao); // Delete the dropdown type
    }



    public DropdownTypeDto daoToDto(DropdownTypeDao dropdownTypeDao) {
        return new DropdownTypeDto(
                dropdownTypeDao.getId(),
                dropdownTypeDao.getVersion(),
                dropdownTypeDao.getDescription(),
                dropdownTypeDao.getName(),
                dropdownTypeDao.getSortOrder()
        );
    }

    public DropdownTypeDao dtoToDao(DropdownTypeDto dropdownTypeDto, DropdownTypeDao dropdownTypeDao) {

        dropdownTypeDao.setId(dropdownTypeDto.id());
        dropdownTypeDao.setVersion(dropdownTypeDto.version());
        dropdownTypeDao.setDescription(dropdownTypeDto.description());
        dropdownTypeDao.setName(dropdownTypeDto.name());
        dropdownTypeDao.setSortOrder(dropdownTypeDto.sortOrder());


        return dropdownTypeDao;
    }

}
