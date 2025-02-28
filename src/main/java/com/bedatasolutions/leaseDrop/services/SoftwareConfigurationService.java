package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.SoftwareConfigurationDao;
import com.bedatasolutions.leaseDrop.dto.SoftwareConfigurationDto;
import com.bedatasolutions.leaseDrop.repo.SoftwareConfigurationRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SoftwareConfigurationService {

    private final SoftwareConfigurationRepo softwareConfigurationRepo;

    // Constructor-based dependency injection for the repository
    public SoftwareConfigurationService(SoftwareConfigurationRepo softwareConfigurationRepo) {
        this.softwareConfigurationRepo = softwareConfigurationRepo;
    }

    // Method to get all software configurations
    public List<SoftwareConfigurationDto> getAllSoftwareConfigurations() {
        List<SoftwareConfigurationDao> softwareConfigurationDaos = softwareConfigurationRepo.findAll(); // Fetch all configurations from the database
        return softwareConfigurationDaos.stream()
                .map(this::daoToDto) // Convert each SoftwareConfigurationDao to SoftwareConfigurationDto
                .collect(Collectors.toList());
    }

    // Method to get a single software configuration by its ID
    public Optional<SoftwareConfigurationDto> getSoftwareConfigurationById(Integer id) {
        SoftwareConfigurationDao softwareConfigurationDao = softwareConfigurationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Software Configuration not found"));
        return Optional.of(daoToDto(softwareConfigurationDao)); // Convert and return as DTO
    }

    // Method to create a new software configuration
    @Transactional
    public SoftwareConfigurationDto create(SoftwareConfigurationDto softwareConfigurationDto) {
        // Convert DTO to DAO
        SoftwareConfigurationDao softwareConfigurationDao = dtoToDao(softwareConfigurationDto,new SoftwareConfigurationDao());

        softwareConfigurationDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        SoftwareConfigurationDao savedSoftwareConfiguration = softwareConfigurationRepo.save(softwareConfigurationDao);
        return daoToDto(savedSoftwareConfiguration); // Return the created software configuration as DTO
    }

    // Method to update an existing software configuration
    @Transactional
    public SoftwareConfigurationDto update( SoftwareConfigurationDto softwareConfigurationDto) {
        SoftwareConfigurationDao existingSoftwareConfiguration = softwareConfigurationRepo.getReferenceById(softwareConfigurationDto.id());

        if (existingSoftwareConfiguration.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Software Configuration not found with id: " +softwareConfigurationDto.id());
        }

        // Save updated software configuration back to the database
        SoftwareConfigurationDao updatedSoftwareConfiguration = softwareConfigurationRepo.save(dtoToDao(softwareConfigurationDto, existingSoftwareConfiguration));
        return daoToDto(updatedSoftwareConfiguration); // Return the updated software configuration as DTO
    }


    // Method to delete a software configuration by ID
    @Transactional
    public void delete(Integer id) {
        SoftwareConfigurationDao softwareConfigurationDao = softwareConfigurationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Software Configuration not found"));
        softwareConfigurationRepo.delete(softwareConfigurationDao); // Delete the software configuration
    }



    public SoftwareConfigurationDto daoToDto(SoftwareConfigurationDao softwareConfigurationDao) {
        return new SoftwareConfigurationDto(
                softwareConfigurationDao.getId(),
                softwareConfigurationDao.getVersion(),
      //          softwareConfigurationDao.getActionKey(),
//                softwareConfigurationDao.getUserMod(),
//                softwareConfigurationDao.getUserAdded(),
                softwareConfigurationDao.getConfigKey(),
                softwareConfigurationDao.getConfigValue()


        );
    }


    public SoftwareConfigurationDao dtoToDao(SoftwareConfigurationDto softwareConfigurationDto, SoftwareConfigurationDao softwareConfigurationDao) {

//        permissionDao.setActionKey(ActionType.UPDATE);
//        permissionDao.setActionType(ActionType.UPDATE);


        softwareConfigurationDao.setId(softwareConfigurationDto.id());
        softwareConfigurationDao.setVersion(softwareConfigurationDto.version());
//        softwareConfigurationDao.setActionKey(softwareConfigurationDto.actionType());
//        softwareConfigurationDao.setUserMod(softwareConfigurationDto.userMod());
//        softwareConfigurationDao.setUserAdded(softwareConfigurationDto.userAdded());
        softwareConfigurationDao.setConfigKey(softwareConfigurationDto.configKey());
        softwareConfigurationDao.setConfigValue(softwareConfigurationDto.configValue());
        return softwareConfigurationDao;
    }
}
