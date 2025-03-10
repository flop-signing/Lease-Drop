package com.bedatasolutions.leaseDrop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonPropertyOrder({
        "url",
        "name",
        "size",
        "dateTime",
        "duration" // Added duration field
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileInfoDto(
        Integer id,
        String url,              // URL of the file
        String name,             // Name of the file
        String size,             // Size of the file
        @JsonIgnore
        String fullPath,        // Full path of the file (for internal use)
        @JsonIgnore
        String path,            // Path to the file (for internal use)
        LocalDateTime dateTime, // Upload date and time
        int duration            // Duration of the file in seconds
) {
}