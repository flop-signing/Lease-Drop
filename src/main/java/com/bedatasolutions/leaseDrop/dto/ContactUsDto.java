package com.bedatasolutions.leaseDrop.dto;

public record ContactUsDto(
        Integer id,
        Integer version,
        String name,
        String email,
        String phone,
        String subject,
        String message,
        String status,
        Boolean response

) {
}
