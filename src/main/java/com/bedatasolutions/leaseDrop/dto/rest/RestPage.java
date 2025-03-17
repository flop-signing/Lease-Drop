package com.bedatasolutions.leaseDrop.dto.rest;

public record RestPage(Integer pageNumber, Integer size) {
    public RestPage() {
        this(1, 10);
    }
}