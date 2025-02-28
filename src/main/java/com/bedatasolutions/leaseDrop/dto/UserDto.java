package com.bedatasolutions.leaseDrop.dto;

public record UserDto (
        Integer id,
        Integer version,
       // Integer roleId,
        String ldapUid,
        String passwordHash,
        String email,
        String username
//        Set<RoleDto> roles

){

}
