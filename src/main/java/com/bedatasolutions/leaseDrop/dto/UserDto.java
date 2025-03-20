package com.bedatasolutions.leaseDrop.dto;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record UserDto (
        Integer id,
        Integer version,

        String ldapUid,
        String passwordHash,
        String email,
        String username,
        String firstName,
        String lastName,
        String companyEmail,
        String contactNumber,
        String address,
        String name,
        String packageType,
        BigDecimal amount,
        LocalDate purchaseDate,
        LocalDate expireDate,
        Integer remainingDays,
        Integer fileProcessing
     //   Set<RoleDto>roles



){

}
