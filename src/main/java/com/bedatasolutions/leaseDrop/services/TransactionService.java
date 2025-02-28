package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.TransactionDao;
import com.bedatasolutions.leaseDrop.dao.UserDao;
import com.bedatasolutions.leaseDrop.dto.TransactionDto;
import com.bedatasolutions.leaseDrop.repo.TransactionRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepository;

    // Constructor-based dependency injection for the repository
    public TransactionService(TransactionRepo transactionRepository) {
        this.transactionRepository = transactionRepository;
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


        // Save updated transaction back to the database
        TransactionDao updatedTransaction = transactionRepository.save(dtoToDao(transactionDto, existingTransaction));
        return daoToDto(updatedTransaction); // Return the updated transaction as DTO
    }

    // Method to delete a transaction by ID
    @Transactional
    public void delete(Integer transactionId) {
        TransactionDao transactionDao = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transactionRepository.delete(transactionDao); // Delete the transaction
    }



    public TransactionDto daoToDto(TransactionDao transactionDao) {
        return new TransactionDto(
                transactionDao.getId(),
                transactionDao.getVersion(),
                transactionDao.getTransactionType(),
                transactionDao.getDetails()

        );
    }
    public TransactionDao dtoToDao(TransactionDto transactionDto, TransactionDao transactionDao) {

//        permissionDao.setActionKey(ActionType.UPDATE);
//        permissionDao.setActionType(ActionType.UPDATE);


        transactionDao.setId(transactionDto.id());
        transactionDao.setVersion(transactionDto.version());
        transactionDao.setTransactionType(transactionDto.transactionType());
        transactionDao.setDetails(transactionDto.details());

        return transactionDao;
    }


}
