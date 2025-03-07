package com.bedatasolutions.leaseDrop.dto;

import jakarta.persistence.Column;

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
        String address
     //   Set<RoleDto>roles



){

}
