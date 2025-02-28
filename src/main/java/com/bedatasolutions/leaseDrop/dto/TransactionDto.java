package com.bedatasolutions.leaseDrop.dto;

public record TransactionDto(
        Integer id,
        Integer version,
        String transactionType,
        String details
       // Integer userId

) {
}
