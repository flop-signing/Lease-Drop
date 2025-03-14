package com.bedatasolutions.leaseDrop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerDto(
        Integer id,
        Integer version,
        String name,
        String packageType,
        BigDecimal amount,
        LocalDate purchaseDate,
        LocalDate expireDate,
        Integer remainingDays,
        Integer fileProcessing

) {
}
