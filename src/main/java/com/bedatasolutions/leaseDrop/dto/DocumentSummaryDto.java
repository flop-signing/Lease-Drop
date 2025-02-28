package com.bedatasolutions.leaseDrop.dto;

public record DocumentSummaryDto (
        Integer id,
        Integer version,
        String summary,
        String metaData
       // Integer documentId later
){


}
