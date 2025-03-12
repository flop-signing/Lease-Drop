package com.bedatasolutions.leaseDrop.dto;

import java.time.LocalDateTime;

public record BannerDto(
        Integer id,
        Integer version,
        Integer duration
) {}