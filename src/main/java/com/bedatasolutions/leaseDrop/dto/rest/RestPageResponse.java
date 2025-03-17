package com.bedatasolutions.leaseDrop.dto.rest;

import org.springframework.data.domain.Page;

import java.util.Collection;

public record RestPageResponse<TDao, TDto>(Collection<TDto> content, int pageNumber, int pageSize, int elements,
                                           long totalElements,
                                           int totalPages) {

    public RestPageResponse(Collection<TDto> content, Page<TDao> page) {
        this(content, page.getPageable().getPageNumber() + 1, page.getPageable().getPageSize(), page.getNumberOfElements(), page.getTotalElements(), page.getTotalPages());
    }
}
