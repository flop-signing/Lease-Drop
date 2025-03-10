package com.bedatasolutions.leaseDrop.dto;

import java.time.LocalDateTime;

public record BannerDto(
        Integer id,
        Integer version,
        String title,
        String size,
        String filePath,
        Integer duration
) {}