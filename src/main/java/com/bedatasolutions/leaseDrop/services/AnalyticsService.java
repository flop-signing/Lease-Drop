package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.AnalyticsDao;
import com.bedatasolutions.leaseDrop.dto.AnalyticsDto;
import com.bedatasolutions.leaseDrop.repo.AnalyticsRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final AnalyticsRepo analyticsRepo;

    public AnalyticsService(AnalyticsRepo analyticsRepo)
    {
        this.analyticsRepo=analyticsRepo;
    }

    // Method to get all analytics
    public List<AnalyticsDto> getAllAnalytics() {
        List<AnalyticsDao> analyticsDaos = analyticsRepo.findAll(); // Fetch all analytics from the database
        return analyticsDaos.stream()
                .map(this::daoToDto) // Convert each AnalyticsDao to AnalyticsDto
                .collect(Collectors.toList());
    }


    // Method to get a single analytics record by its ID
    public Optional<AnalyticsDto> getAnalyticsById(Integer id) {
        AnalyticsDao analyticsDao = analyticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Analytics not found"));
        return Optional.of(daoToDto(analyticsDao)); // Convert and return as DTO
    }



    @Transactional
    // Method to create a new analytics record
    public AnalyticsDto create(AnalyticsDto analyticsDto) {

        // Save the entity to the database

        AnalyticsDao analyticsDao = dtoToDao(analyticsDto, new AnalyticsDao());
        analyticsDao.setActionKey(ActionType.CREATE);
        AnalyticsDao savedAnalytics=analyticsRepo.save(analyticsDao);
        return daoToDto(savedAnalytics); // Return the created analytics record as DTO

    }


    @Transactional
    // Method to update an existing analytics record
    public AnalyticsDto update(AnalyticsDto analyticsDto) {
        AnalyticsDao existingAnalytics = analyticsRepo.getReferenceById(analyticsDto.id());

        if (existingAnalytics.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Analytics Setting not found with id: " + analyticsDto.id());
        }

        existingAnalytics.setActionKey(ActionType.UPDATE);
        AnalyticsDao updatedAnalytics=analyticsRepo.save(dtoToDao(analyticsDto,existingAnalytics));

        return daoToDto(updatedAnalytics);



    }


    @Transactional
    // Method to delete an analytics record by ID
    public void delete(Integer id) {
        AnalyticsDao analyticsDao = analyticsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Analytics not found"));
        analyticsDao.setActionKey(ActionType.DELETE);
        analyticsRepo.delete(analyticsDao); // Delete the analytics record
    }


    public AnalyticsDto daoToDto(AnalyticsDao analyticsDao) {
        return new AnalyticsDto(
                analyticsDao.getId(),
                analyticsDao.getVersion(),
                analyticsDao.getData(),
                analyticsDao.getUsers().getId() //FK
        );
    }

    public AnalyticsDao dtoToDao(AnalyticsDto analyticsDto, AnalyticsDao analyticsDao) {

        analyticsDao.setId(analyticsDto.id());
        analyticsDao.setVersion(analyticsDto.version());
        analyticsDao.setData(analyticsDto.data());
        //analyticsDao.setUsers();

        return analyticsDao;
    }

}
