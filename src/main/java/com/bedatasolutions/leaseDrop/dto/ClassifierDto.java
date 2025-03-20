package com.bedatasolutions.leaseDrop.dto;

import com.bedatasolutions.leaseDrop.constants.db.FieldType;

import java.util.List;

public record ClassifierDto(
        Integer id,
        Integer version,
        String name,
        String groupKey,
        String description,
        FieldType type,
        boolean isActive,
        List<String> relation  // List of relations
) {}