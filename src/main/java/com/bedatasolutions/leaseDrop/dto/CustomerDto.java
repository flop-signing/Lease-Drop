package com.bedatasolutions.leaseDrop.dto;

import java.time.LocalDate;

public record CustomerDto(
        Integer id,
        Integer version,
        String name,
        String packageType,
        String amount,
        LocalDate purchaseDate,
        LocalDate expireDate,
        String remainingDays,
        Integer fileProcessing

) {
}
