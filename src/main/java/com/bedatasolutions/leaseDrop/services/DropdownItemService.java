package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.DropdownItemDao;
import com.bedatasolutions.leaseDrop.dto.DropdownItemDto;
import com.bedatasolutions.leaseDrop.repo.DropdownItemRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DropdownItemService {

    private final DropdownItemRepo dropdownItemRepo;

    // Constructor-based dependency injection for the repository
    public DropdownItemService(DropdownItemRepo dropdownItemRepo) {
        this.dropdownItemRepo = dropdownItemRepo;
    }

    // Method to get all dropdown items
    public List<DropdownItemDto> getAllDropdownItems() {
        List<DropdownItemDao> dropdownItemDaos = dropdownItemRepo.findAll(); // Fetch all dropdown items from the database
        return dropdownItemDaos.stream()
                .map(this::daoToDto) // Convert each DropdownItemDao to DropdownItemDto
                .collect(Collectors.toList());
    }

    // Method to get a single dropdown item by its ID
    public Optional<DropdownItemDto> getDropdownItemById(Integer id) {
        DropdownItemDao dropdownItemDao = dropdownItemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Dropdown Item not found"));
        return Optional.of(daoToDto(dropdownItemDao)); // Convert and return as DTO
    }

    // Method to create a new dropdown item
    @Transactional
    public DropdownItemDto create(DropdownItemDto dropdownItemDto) {
        // Convert DTO to DAO
        DropdownItemDao dropdownItemDao = dtoToDao(dropdownItemDto,new DropdownItemDao());

        dropdownItemDao.setActionKey(ActionType.CREATE);

        // Save the entity to the database
        DropdownItemDao savedDropdownItem = dropdownItemRepo.save(dropdownItemDao);
        return daoToDto(savedDropdownItem); // Return the created dropdown item as DTO

    }

    // Method to update an existing dropdown item
    @Transactional
    public DropdownItemDto update(DropdownItemDto dropdownItemDto) {
        DropdownItemDao existingDropdownItem = dropdownItemRepo.getReferenceById(dropdownItemDto.id());

        if (existingDropdownItem.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DropDown Item not found with id: " + dropdownItemDto.id());
        }

        DropdownItemDao updatedDropdownItem=dropdownItemRepo.save(dtoToDao(dropdownItemDto,existingDropdownItem));
        return daoToDto(updatedDropdownItem);

    }
    // Method to delete a dropdown item by ID
    @Transactional
    public boolean delete(Integer id) {
        DropdownItemDao dropdownItemDao = dropdownItemRepo.findById(id).orElse(null);
        if (dropdownItemDao == null) {
            return false; // Return false if the dropdown item is not found
        }

        dropdownItemRepo.delete(dropdownItemDao); // Delete the dropdown item
        return true; // Return true to indicate successful deletion
    }



    public DropdownItemDto daoToDto(DropdownItemDao dropdownItemDao) {
        return new DropdownItemDto(
                dropdownItemDao.getId(),
                dropdownItemDao.getVersion(),
                dropdownItemDao.getBgColor(),
                dropdownItemDao.getDescription(),
                dropdownItemDao.getName(),
                dropdownItemDao.getSortOrder(),
                dropdownItemDao.getTextColor()

        );
    }

    public DropdownItemDao dtoToDao(DropdownItemDto dropdownItemDto, DropdownItemDao dropdownItemDao) {

        dropdownItemDao.setId(dropdownItemDto.id());
        dropdownItemDao.setVersion(dropdownItemDto.version());
        dropdownItemDao.setBgColor(dropdownItemDto.bgColor());
        dropdownItemDao.setDescription(dropdownItemDto.description());
        dropdownItemDao.setName(dropdownItemDto.name());
        dropdownItemDao.setSortOrder(dropdownItemDto.sortOrder());
        dropdownItemDao.setTextColor(dropdownItemDto.textColor());

        return dropdownItemDao;
    }

}
