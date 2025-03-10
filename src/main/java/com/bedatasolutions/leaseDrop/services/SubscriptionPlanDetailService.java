package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.SubscriptionPlanDetailDao;
import com.bedatasolutions.leaseDrop.dto.SubscriptionPlanDetailDto;
import com.bedatasolutions.leaseDrop.repo.SubscriptionPlanDetailRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanDetailService {

    private final SubscriptionPlanDetailRepo subscriptionPlanDetailRepo;

    public SubscriptionPlanDetailService(SubscriptionPlanDetailRepo subscriptionPlanDetailRepo) {
        this.subscriptionPlanDetailRepo = subscriptionPlanDetailRepo;
    }


    // Method to get all subscriptions
    public List<SubscriptionPlanDetailDto> getAllSubscriptionPlanDetails() {
        List<SubscriptionPlanDetailDao> subscriptionPlanDetailDaos = subscriptionPlanDetailRepo.findAll(); // Fetch all subscriptions from the database
        return subscriptionPlanDetailDaos.stream()
                .map(this::daoToDto) // Convert each SubscriptionDao to SubscriptionDto
                .collect(Collectors.toList());
    }

    // Method to get a single subscription by its ID
    public Optional<SubscriptionPlanDetailDto> getSubscriptionPlanByDetailId(Integer id) {
        SubscriptionPlanDetailDao subscriptionPlanDetailDao = subscriptionPlanDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription Plan Detail not found"));
        return Optional.of(daoToDto(subscriptionPlanDetailDao)); // Convert and return as DTO
    }

    // Method to create a new subscription
    @Transactional
    public SubscriptionPlanDetailDto create(SubscriptionPlanDetailDto subscriptionPlanDetailDto) {
        // Convert DTO to DAO
        SubscriptionPlanDetailDao subscriptionPlanDetailDao = dtoToDao(subscriptionPlanDetailDto,new SubscriptionPlanDetailDao());

        subscriptionPlanDetailDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        SubscriptionPlanDetailDao savedSubscriptionPlanDetail = subscriptionPlanDetailRepo.save(subscriptionPlanDetailDao);
        return daoToDto(savedSubscriptionPlanDetail); // Return the created subscription as DTO
    }

    @Transactional
    public SubscriptionPlanDetailDto update( SubscriptionPlanDetailDto subscriptionPlanDetailDto) {
        SubscriptionPlanDetailDao existingSubscriptionPlanDetail = subscriptionPlanDetailRepo.getReferenceById(subscriptionPlanDetailDto.id());


        if (existingSubscriptionPlanDetail.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription Plan Detail not found with name: " +subscriptionPlanDetailDto.id());
        }

        existingSubscriptionPlanDetail.setActionKey(ActionType.UPDATE);

        // Save updated subscription back to the database
        SubscriptionPlanDetailDao updatedSubscriptionPlanDetail = subscriptionPlanDetailRepo.save(dtoToDao(subscriptionPlanDetailDto, existingSubscriptionPlanDetail));
        return daoToDto(updatedSubscriptionPlanDetail); // Return the updated subscription as DTO
    }


    // Method to delete a subscription by ID
    @Transactional
    public void delete(Integer id) {
        SubscriptionPlanDetailDao subscriptionPlanDetailDao = subscriptionPlanDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription Plan Detail not found"));

        subscriptionPlanDetailDao.setActionKey(ActionType.DELETE);
        subscriptionPlanDetailRepo.delete(subscriptionPlanDetailDao); // Delete the subscription
    }


    public SubscriptionPlanDetailDto daoToDto(SubscriptionPlanDetailDao subscriptionPlanDetailDao) {
        return new SubscriptionPlanDetailDto(

                subscriptionPlanDetailDao.getId(),
                subscriptionPlanDetailDao.getVersion(),
                subscriptionPlanDetailDao.getName(),
                subscriptionPlanDetailDao.getOwner(),
                subscriptionPlanDetailDao.getDocstatus(),
                subscriptionPlanDetailDao.getIdx(),
                subscriptionPlanDetailDao.getPlan(),
                subscriptionPlanDetailDao.getQty(),
                subscriptionPlanDetailDao.getParent(),
                subscriptionPlanDetailDao.getParentField(),
                subscriptionPlanDetailDao.getParentType()
     //           subscriptionPlanDetailDao.getSubscriptionPlan().getPlanName()

        );
    }




    public SubscriptionPlanDetailDao dtoToDao(SubscriptionPlanDetailDto subscriptionPlanDetailDto, SubscriptionPlanDetailDao subscriptionPlanDetailDao) {


        subscriptionPlanDetailDao.setId(subscriptionPlanDetailDto.id());
        subscriptionPlanDetailDao.setVersion(subscriptionPlanDetailDto.version());
        subscriptionPlanDetailDao.setName(subscriptionPlanDetailDto.name());
        subscriptionPlanDetailDao.setOwner(subscriptionPlanDetailDao.getOwner());
        subscriptionPlanDetailDao.setDocstatus(subscriptionPlanDetailDao.getDocstatus());
        subscriptionPlanDetailDao.setIdx(subscriptionPlanDetailDao.getIdx());
        subscriptionPlanDetailDao.setPlan(subscriptionPlanDetailDao.getPlan());
        subscriptionPlanDetailDao.setQty(subscriptionPlanDetailDao.getQty());
        subscriptionPlanDetailDao.setParent(subscriptionPlanDetailDao.getParent());
        subscriptionPlanDetailDao.setParentField(subscriptionPlanDetailDao.getParentField());
        subscriptionPlanDetailDao.setParentType(subscriptionPlanDetailDao.getParentType());
      //  subscriptionPlanDetailDao.setSubscriptionPlan(subscriptionPlanDetailDao.getSubscriptionPlan());



        return subscriptionPlanDetailDao;
    }






}
