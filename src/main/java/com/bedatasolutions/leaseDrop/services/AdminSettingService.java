package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.AdminSettingDao;
import com.bedatasolutions.leaseDrop.dao.AnalyticsDao;
import com.bedatasolutions.leaseDrop.dao.PermissionDao;
import com.bedatasolutions.leaseDrop.dto.AdminSettingDto;
import com.bedatasolutions.leaseDrop.dto.PermissionDto;
import com.bedatasolutions.leaseDrop.repo.AdminSettingRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminSettingService {
    private final AdminSettingRepo adminSettingRepo;

    public AdminSettingService(AdminSettingRepo adminSettingRepo)
    {
        this.adminSettingRepo=adminSettingRepo;
    }

    // Method to get all admin settings
    public List<AdminSettingDto> getAllAdminSettings() {
        List<AdminSettingDao> adminSettingDaos = adminSettingRepo.findAll(); // Fetch all admin settings from the database
        return adminSettingDaos.stream()
                .map(this::daoToDto) // Convert each AdminSettingDao to AdminSettingDto
                .collect(Collectors.toList());
    }


    // Method to get a single admin setting by its ID
    public Optional<AdminSettingDto> getAdminSettingById(Integer id) {
        AdminSettingDao adminSettingDao = adminSettingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin setting not found"));
        return Optional.of(daoToDto(adminSettingDao)); // Convert and return as DTO
    }



    // Method to create a new admin setting
    @Transactional
    public AdminSettingDto create(AdminSettingDto adminSettingDto) {

        AdminSettingDao adminSettingDao = dtoToDao(adminSettingDto,new AdminSettingDao());
        adminSettingDao.setActionKey(ActionType.CREATE);
        AdminSettingDao saveAdminSetting =adminSettingRepo.save(adminSettingDao);
        return daoToDto(saveAdminSetting); // Return the created analytics record as DTO

    }

    // Method to update an existing admin setting
    @Transactional
    public AdminSettingDto update(AdminSettingDto adminSettingDto) {

        AdminSettingDao existingAdminSetting = adminSettingRepo.getReferenceById(adminSettingDto.id());
        if (existingAdminSetting.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin Setting not found with id: " + adminSettingDto.id());
        }

        AdminSettingDao updatedAdminSetting=adminSettingRepo.save(dtoToDao(adminSettingDto,existingAdminSetting));
        return daoToDto(updatedAdminSetting);
    }


    // Method to delete an admin setting by ID
    @Transactional
    public void delete(Integer id) {
        AdminSettingDao adminSettingDao = adminSettingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin setting not found"));
        adminSettingRepo.delete(adminSettingDao); // Delete the admin setting
    }




    public AdminSettingDto daoToDto(AdminSettingDao adminSettingDao) {
        return new AdminSettingDto(
                adminSettingDao.getId(),
                adminSettingDao.getVersion(),
                adminSettingDao.getSettingsKey(),
                adminSettingDao.getSettingValue(),
                adminSettingDao.getEmailNotifications(),
                adminSettingDao.getTempFileDuration(),
                adminSettingDao.getCacheDuration(),
                adminSettingDao.getSubscriptionRemainderDuration(),
                adminSettingDao.getWelcomeMail(),
                adminSettingDao.getOtpExpiry()

        );
    }

    public AdminSettingDao dtoToDao(AdminSettingDto adminSettingDto, AdminSettingDao adminSettingDao) {

        adminSettingDao.setId(adminSettingDto.id());
        adminSettingDao.setVersion(adminSettingDto.version());
        adminSettingDao.setSettingsKey(adminSettingDto.settingsKey());
        adminSettingDao.setSettingValue(adminSettingDto.settingValue());
        adminSettingDao.setEmailNotifications(adminSettingDto.emailNotifications());
        adminSettingDao.setTempFileDuration(adminSettingDto.tempFileDuration());
        adminSettingDao.setCacheDuration(adminSettingDto.cacheDuration());
        adminSettingDao.setSubscriptionRemainderDuration(adminSettingDto.subscriptionRemainderDuration());
        adminSettingDao.setWelcomeMail(adminSettingDto.welcomeMail());
        adminSettingDao.setOtpExpiry(adminSettingDto.otpExpiry());

        return adminSettingDao;
    }

}
