package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.SubscriptionDao;
import com.bedatasolutions.leaseDrop.dto.SubscriptionDto;
import com.bedatasolutions.leaseDrop.repo.SubscriptionRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepo subscriptionRepo;

    // Constructor-based dependency injection for the repository
    public SubscriptionService(SubscriptionRepo subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    // Method to get all subscriptions
    public List<SubscriptionDto> getAllSubscriptions() {
        List<SubscriptionDao> subscriptionDaos = subscriptionRepo.findAll(); // Fetch all subscriptions from the database
        return subscriptionDaos.stream()
                .map(this::daoToDto) // Convert each SubscriptionDao to SubscriptionDto
                .collect(Collectors.toList());
    }

    // Method to get a single subscription by its ID
    public Optional<SubscriptionDto> getSubscriptionById(Integer id) {
        SubscriptionDao subscriptionDao = subscriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return Optional.of(daoToDto(subscriptionDao)); // Convert and return as DTO
    }

    // Method to create a new subscription
    @Transactional
    public SubscriptionDto create(SubscriptionDto subscriptionDto) {
        // Convert DTO to DAO
        SubscriptionDao subscriptionDao = dtoToDao(subscriptionDto,new SubscriptionDao());

        subscriptionDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        SubscriptionDao savedSubscription = subscriptionRepo.save(subscriptionDao);
        return daoToDto(savedSubscription); // Return the created subscription as DTO
    }

    // Method to update an existing subscription
    @Transactional
    public SubscriptionDto update( SubscriptionDto subscriptionDto) {
        SubscriptionDao existingSubscription = subscriptionRepo.getReferenceById(subscriptionDto.id());


        if (existingSubscription.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found with id: " +subscriptionDto.id());
        }

        // Save updated subscription back to the database
        SubscriptionDao updatedSubscription = subscriptionRepo.save(dtoToDao(subscriptionDto, existingSubscription));
        return daoToDto(updatedSubscription); // Return the updated subscription as DTO
    }

    // Method to delete a subscription by ID
    @Transactional
    public void delete(Integer id) {
        SubscriptionDao subscriptionDao = subscriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscriptionRepo.delete(subscriptionDao); // Delete the subscription
    }


    public SubscriptionDto daoToDto(SubscriptionDao subscriptionDao) {
        return new SubscriptionDto(
                subscriptionDao.getId(),
                subscriptionDao.getVersion(),
                subscriptionDao.getPlanType(),
                subscriptionDao.getDocumentLimit(),
                subscriptionDao.getStripeCustomerId(),
                subscriptionDao.getStripeSubscriptionId(),
                subscriptionDao.getStatus()

        );
    }


    public SubscriptionDao dtoToDao(SubscriptionDto subscriptionDto, SubscriptionDao subscriptionDao) {

//        permissionDao.setActionKey(ActionType.UPDATE);
//        permissionDao.setActionType(ActionType.UPDATE);


        subscriptionDao.setId(subscriptionDto.id());
        subscriptionDao.setVersion(subscriptionDto.version());
        subscriptionDao.setPlanType(subscriptionDto.planType());
        subscriptionDao.setDocumentLimit(subscriptionDto.documentLimit());
        subscriptionDao.setStripeCustomerId(subscriptionDto.stripeCustomerId());
        subscriptionDao.setStripeSubscriptionId(subscriptionDto.stripeSubscriptionId());

        subscriptionDao.setStatus(subscriptionDto.status());
        return subscriptionDao;
    }

}
