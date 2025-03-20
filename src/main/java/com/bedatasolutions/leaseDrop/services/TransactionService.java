package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.TransactionDao;
import com.bedatasolutions.leaseDrop.dao.UserDao;
import com.bedatasolutions.leaseDrop.dto.TransactionDto;
import com.bedatasolutions.leaseDrop.repo.TransactionRepo;
import com.bedatasolutions.leaseDrop.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepository;
    private final UserRepo userRepo;

    // Constructor-based dependency injection for the repository

    public TransactionService(TransactionRepo transactionRepository,UserRepo userRepo) {
        this.transactionRepository = transactionRepository;
        this.userRepo = userRepo;
    }

    // Method to get all transactions
    public List<TransactionDto> getAllTransactions() {
        List<TransactionDao> transactionDaos = transactionRepository.findAll(); // Fetch all transactions from the database
        return transactionDaos.stream()
                .map(this::daoToDto) // Convert each TransactionDao to TransactionDto
                .collect(Collectors.toList());
    }

    // Method to get a single transaction by its ID
    public Optional<TransactionDto> getTransactionById(Integer transactionId) {
        TransactionDao transactionDao = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return Optional.of(daoToDto(transactionDao)); // Convert and return as DTO
    }

    // Method to create a new transaction
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        TransactionDao transactionDao = dtoToDao(transactionDto,new TransactionDao());

        transactionDao.setActionKey(ActionType.CREATE);
        // Save the entity to the database
        TransactionDao savedTransaction = transactionRepository.save(transactionDao);
        return daoToDto(savedTransaction); // Return the created transaction as DTO

    }

    // Method to update an existing transaction
    @Transactional
    public TransactionDto update( TransactionDto transactionDto) {
        TransactionDao existingTransaction = transactionRepository.getReferenceById(transactionDto.id());

        if (existingTransaction.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found with id: " +transactionDto.id());
        }
        existingTransaction.setActionKey(ActionType.UPDATE);


        // Save updated transaction back to the database
        TransactionDao updatedTransaction = transactionRepository.save(dtoToDao(transactionDto, existingTransaction));
        return daoToDto(updatedTransaction); // Return the updated transaction as DTO
    }

    // Method to delete a transaction by ID
    @Transactional
    public boolean delete(Integer transactionId) {
        TransactionDao transactionDao = transactionRepository.findById(transactionId).orElse(null);
        if (transactionDao == null) {
            return false; // Return false if the transaction is not found
        }

        transactionDao.setActionKey(ActionType.DELETE);
        transactionRepository.delete(transactionDao); // Delete the transaction
        return true; // Return true to indicate successful deletion
    }



    public TransactionDto daoToDto(TransactionDao transactionDao) {
        return new TransactionDto(
                transactionDao.getId(),
                transactionDao.getVersion(),
                transactionDao.getTransactionType(),
                transactionDao.getDetails(),
                transactionDao.getUsers().getId()

        );
    }
    public TransactionDao dtoToDao(TransactionDto transactionDto, TransactionDao transactionDao) {

//        permissionDao.setActionKey(ActionType.UPDATE);
//        permissionDao.setActionType(ActionType.UPDATE);


        transactionDao.setId(transactionDto.id());
        transactionDao.setVersion(transactionDto.version());
        transactionDao.setTransactionType(transactionDto.transactionType());
        transactionDao.setDetails(transactionDto.details());

        UserDao user = userRepo.findById(transactionDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + transactionDto.userId()));

        transactionDao.setUsers(user);


        return transactionDao;
    }


}
