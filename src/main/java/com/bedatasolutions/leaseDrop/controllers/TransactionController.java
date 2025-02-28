package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.dto.TransactionDto;
import com.bedatasolutions.leaseDrop.services.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions") // Base URL for transaction-related API endpoints
@SecurityRequirement(name = "LeaseDrop-sec")
@Tag(name = "11. Transaction Controller", description = "Manage Transaction Records")
public class TransactionController {


    private final TransactionService transactionService;  // Inject the TransactionService to access business logic

    public TransactionController(TransactionService transactionService)
    {
        this.transactionService=transactionService;
    }
    // Endpoint to get all transactions
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();  // Get all transactions from the service
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Endpoint to get a transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Integer id) {
        Optional<TransactionDto> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new transaction
    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        TransactionDto createdTransaction = transactionService.create(transactionDto);  // Create transaction
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);  // Return the created transaction with HTTP 201
    }

    // Endpoint to update an existing transaction
    @PutMapping()
    public ResponseEntity<TransactionDto> updateTransaction( @RequestBody TransactionDto transactionDto) {
        try {
            TransactionDto updatedTransaction = transactionService.update(transactionDto);  // Update transaction
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if transaction not found
        }
    }

    // Endpoint to delete a transaction by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        try {
            transactionService.delete(id);  // Delete transaction
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return 204 (No Content)
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if transaction not found
        }
    }
}
