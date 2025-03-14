package com.bedatasolutions.leaseDrop.criteria;

import java.util.Map;

public class FilterHelper {

    // Generic specification builder using reflection
    public static <T> void addFilter(Map<String, Object> filters, String field, T value) {
        if (value != null && (value instanceof String ? !((String) value).trim().isEmpty() : true)) {
            filters.put(field, value);
        }
    }
}
