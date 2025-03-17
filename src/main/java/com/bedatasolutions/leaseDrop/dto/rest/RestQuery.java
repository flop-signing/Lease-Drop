package com.bedatasolutions.leaseDrop.dto.rest;

import java.util.Map;

public record RestQuery(Map<String, String> filter, RestSort sort, RestPage page) {
}
