package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.NotificationDao;
import com.bedatasolutions.leaseDrop.dao.UserDao;
import com.bedatasolutions.leaseDrop.dto.NotificationDto;
import com.bedatasolutions.leaseDrop.repo.NotificationRepo;
import com.bedatasolutions.leaseDrop.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;

    private final UserRepo userRepo;

    // Constructor-based dependency injection for the repository
    public NotificationService(NotificationRepo notificationRepo,UserRepo userRepo) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
    }

    // Method to get all notifications
    public List<NotificationDto> getAllNotifications() {
        List<NotificationDao> notificationDaos = notificationRepo.findAll(); // Fetch all notifications from the database
        return notificationDaos.stream()
                .map(this::daoToDto) // Convert each NotificationDao to NotificationDto
                .collect(Collectors.toList());
    }

    // Method to get a single notification by its ID
    public Optional<NotificationDto> getNotificationById(Integer id) {
        NotificationDao notificationDao = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return Optional.of(daoToDto(notificationDao)); // Convert and return as DTO
    }

    // Method to create a new notification
    @Transactional
    public NotificationDto create(NotificationDto notificationDto) {
        // Convert DTO to DAO
        NotificationDao notificationDao = dtoToDao(notificationDto,new NotificationDao());

        notificationDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        NotificationDao savedNotification = notificationRepo.save(notificationDao);
        return daoToDto(savedNotification); // Return the created notification as DTO
    }

    // Method to update an existing notification
    @Transactional
    public NotificationDto update(NotificationDto notificationDto) {
        NotificationDao existingNotification = notificationRepo.getReferenceById(notificationDto.id());

        if (existingNotification.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found with id: " + notificationDto.id());
        }

        // Save updated notification back to the database
        NotificationDao updatedNotification = notificationRepo.save(dtoToDao(notificationDto,existingNotification));
        existingNotification.setActionKey(ActionType.UPDATE);
        return daoToDto(updatedNotification); // Return the updated notification as DTO

    }

    // Method to delete a notification by ID
    @Transactional
    public boolean delete(Integer id) {
        NotificationDao notificationDao = notificationRepo.findById(id).orElse(null);
        if (notificationDao == null) {
            return false; // Return false if the notification is not found
        }

        notificationDao.setActionKey(ActionType.DELETE);
        notificationRepo.delete(notificationDao); // Delete the notification
        return true; // Return true to indicate successful deletion
    }


    public NotificationDto daoToDto(NotificationDao notificationDao) {
        return new NotificationDto(
                notificationDao.getId(),
                notificationDao.getVersion(),
                notificationDao.getContent(),
                notificationDao.getChannel(),
                notificationDao.getSubject(),
                notificationDao.getSender(),
                notificationDao.getSenderEmail(),
                notificationDao.getUsers().getId()
        );
    }

    public NotificationDao dtoToDao(NotificationDto notificationDto, NotificationDao notificationDao) {

        notificationDao.setId(notificationDto.id());
        notificationDao.setVersion(notificationDto.version());
        notificationDao.setContent(notificationDto.content());
        notificationDao.setChannel(notificationDto.channel());
        notificationDao.setSubject(notificationDto.subject());
        notificationDao.setSender(notificationDto.sender());
        notificationDao.setSenderEmail(notificationDto.senderEmail());

        UserDao user = userRepo.findById(notificationDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + notificationDto.userId()));

        notificationDao.setUsers(user);

        return notificationDao;
    }

}
