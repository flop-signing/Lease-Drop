package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.SubscriptionPlanDao;
import com.bedatasolutions.leaseDrop.dto.SubscriptionPlanDto;
import com.bedatasolutions.leaseDrop.repo.SubscriptionPlanRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanService {

    private final SubscriptionPlanRepo subscriptionPlanRepo;

    public SubscriptionPlanService(SubscriptionPlanRepo subscriptionPlanRepo) {
        this.subscriptionPlanRepo = subscriptionPlanRepo;
    }


    // Method to get all subscriptions
    public List<SubscriptionPlanDto> getAllSubscriptions() {
        List<SubscriptionPlanDao> subscriptionPlanDaos = subscriptionPlanRepo.findAll(); // Fetch all subscriptions from the database
        return subscriptionPlanDaos.stream()
                .map(this::daoToDto) // Convert each SubscriptionDao to SubscriptionDto
                .collect(Collectors.toList());
    }

    // Method to get a single subscription by its ID
    public Optional<SubscriptionPlanDto> getSubscriptionPlanById(Integer id) {
        SubscriptionPlanDao subscriptionPlanDao = subscriptionPlanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription Plan not found"));
        return Optional.of(daoToDto(subscriptionPlanDao)); // Convert and return as DTO
    }

    // Method to create a new subscription
    @Transactional
    public SubscriptionPlanDto create(SubscriptionPlanDto subscriptionPlanDto) {
        // Convert DTO to DAO
        SubscriptionPlanDao subscriptionPlanDao = dtoToDao(subscriptionPlanDto,new SubscriptionPlanDao());

        subscriptionPlanDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        SubscriptionPlanDao savedSubscriptionPlan = subscriptionPlanRepo.save(subscriptionPlanDao);
        return daoToDto(savedSubscriptionPlan); // Return the created subscription as DTO
    }

    @Transactional
    public SubscriptionPlanDto update(SubscriptionPlanDto subscriptionPlanDto) {
        SubscriptionPlanDao existingSubscriptionPlan = subscriptionPlanRepo.getReferenceById(subscriptionPlanDto.id());


        if (existingSubscriptionPlan.getName() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription Plan not found with name: " +subscriptionPlanDto.name());
        }

        existingSubscriptionPlan.setActionKey(ActionType.UPDATE);

        // Save updated subscription back to the database
        SubscriptionPlanDao updatedSubscriptionPlan = subscriptionPlanRepo.save(dtoToDao(subscriptionPlanDto, existingSubscriptionPlan));
        return daoToDto(updatedSubscriptionPlan); // Return the updated subscription as DTO
    }


    // Method to delete a subscription plan by ID
    @Transactional
    public boolean delete(Integer id) {
        SubscriptionPlanDao subscriptionPlanDao = subscriptionPlanRepo.findById(id).orElse(null);
        if (subscriptionPlanDao == null) {
            return false; // Return false if the subscription plan is not found
        }

        subscriptionPlanDao.setActionKey(ActionType.DELETE);
        subscriptionPlanRepo.delete(subscriptionPlanDao); // Delete the subscription plan
        return true; // Return true to indicate successful deletion
    }


    public SubscriptionPlanDto daoToDto(SubscriptionPlanDao subscriptionPlanDao) {
        return new SubscriptionPlanDto(
                subscriptionPlanDao.getId(),
                subscriptionPlanDao.getVersion(),
                subscriptionPlanDao.getName(),
                subscriptionPlanDao.getOwner(),
                subscriptionPlanDao.getDocstatus(),
                subscriptionPlanDao.getIdx(),
                subscriptionPlanDao.getPlanName(),
                subscriptionPlanDao.getCurrency(),
                subscriptionPlanDao.getItem(),
                subscriptionPlanDao.getPriceDetermination(),
                subscriptionPlanDao.getCost(),
                subscriptionPlanDao.getPriceList(),
                subscriptionPlanDao.getBillingInterval(),
                subscriptionPlanDao.getBillingIntervalCount(),
                subscriptionPlanDao.getProductPriceId(),
                subscriptionPlanDao.getPaymentGateway(),
                subscriptionPlanDao.getCostCenter(),
                subscriptionPlanDao.getUserTags(),
                subscriptionPlanDao.getComments(),
                subscriptionPlanDao.getAssign(),
                subscriptionPlanDao.getLikedBy()
//                subscriptionPlanDao.getSubscriptionPlanDetail().getName()

        );
    }



    public SubscriptionPlanDao dtoToDao(SubscriptionPlanDto subscriptionPlanDto, SubscriptionPlanDao subscriptionPlanDao) {


        subscriptionPlanDao.setId(subscriptionPlanDto.id());
        subscriptionPlanDao.setVersion(subscriptionPlanDto.version());
        subscriptionPlanDao.setName(subscriptionPlanDto.name());
        subscriptionPlanDao.setOwner(subscriptionPlanDao.getOwner());
        subscriptionPlanDao.setDocstatus(subscriptionPlanDao.getDocstatus());
        subscriptionPlanDao.setIdx(subscriptionPlanDao.getIdx());
        subscriptionPlanDao.setPlanName(subscriptionPlanDao.getPlanName());
        subscriptionPlanDao.setCurrency(subscriptionPlanDao.getCurrency());
        subscriptionPlanDao.setItem(subscriptionPlanDao.getItem());
        subscriptionPlanDao.setCost(subscriptionPlanDao.getCost());
        subscriptionPlanDao.setCostCenter(subscriptionPlanDao.getCostCenter());
        subscriptionPlanDao.setUserTags(subscriptionPlanDao.getUserTags());
        subscriptionPlanDao.setComments(subscriptionPlanDao.getComments());
        subscriptionPlanDao.setAssign(subscriptionPlanDao.getAssign());
        subscriptionPlanDao.setLikedBy(subscriptionPlanDao.getLikedBy());
//        subscriptionPlanDao.setSubscriptionPlanDetail(subscriptionPlanDao.getSubscriptionPlanDetail());

        return subscriptionPlanDao;
    }






}
