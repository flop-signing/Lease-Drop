package com.bedatasolutions.leaseDrop.dto;


import java.time.LocalDate;

public record BannerDto(
        Integer id,
      //  Integer version,
        LocalDate createdAt,
        Integer duration,
        String fileName,
        String fileSize,
        String url
) {

    // Method to return a new BannerDto with the updated URL
    public BannerDto url(String newUrl) {
        return new BannerDto(id, createdAt, duration, fileName, fileSize, newUrl);
    }


}