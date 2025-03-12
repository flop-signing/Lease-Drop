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
        String url,
        String name,
        String size,
        Integer duration ,
        LocalDateTime date
) {
}