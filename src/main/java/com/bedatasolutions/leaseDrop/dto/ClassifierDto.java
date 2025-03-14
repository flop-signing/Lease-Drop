package com.bedatasolutions.leaseDrop.dto;

import java.util.List;

import com.bedatasolutions.leaseDrop.constants.db.FieldType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record ClassifierDto(
        Integer id,
        Integer version,
        String name,
        String groupKey,
        String description,
        FieldType type,
        boolean isActive,
        List<String> relation // Relation is stored as a List<String> in the DTO
) {

    // Convert the List<String> to a CSV string
    public String getRelationCsv() {
        return String.join(",", this.relation);  // Convert List to CSV string
    }

    // Convert the CSV string back to List<String>
    public static List<String> convertCsvToList(String relationCsv) {
        return List.of(relationCsv.split(","));  // Convert CSV string back to List
    }

    // Optional: A factory method for converting from CSV to DTO
    public static ClassifierDto fromCsv(Integer id, Integer version, String name, String groupKey, String description, FieldType type, boolean isActive, String relationCsv) {
        List<String> relationList = convertCsvToList(relationCsv);
        return new ClassifierDto(id, version, name, groupKey, description, type, isActive, relationList);
    }
}
