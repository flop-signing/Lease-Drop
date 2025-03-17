package com.bedatasolutions.leaseDrop.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ClassMapper {


    // Static map to store column types
    private static final Map<String, Class<?>> COLUMN_TYPE_MAP = new HashMap<>();

    // Method to build the column type map
    public static Map<String, Class<?>> buildColumnTypeMap(Class<?> clazz) {
        // Use reflection to populate the map
        // Example: COLUMN_TYPE_MAP.put("columnName", clazz.getDeclaredField("columnName").getType());
        // This is a simplified example; you would need to implement the reflection logic.
        return COLUMN_TYPE_MAP;
    }

    public static  <T> Map<String, Class<?>> buildColumnTypeMap(T typeClass) {
        Map<String, Class<?>> columnTypeMap = new HashMap<>();
        for (Field field : typeClass.getClass().getDeclaredFields()) {
            columnTypeMap.put(field.getName(), field.getType());
        }
        return columnTypeMap;
    }

    public static Object convertValue(String value, Class<?> targetType) {
        if (targetType == null) {
            throw new IllegalArgumentException("Unsupported filter key. Valid keys are: " + COLUMN_TYPE_MAP.keySet());
        }
        if (targetType == String.class) {
            return value;
        } else if (targetType == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (targetType == LocalDate.class) {
            return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
        } else if (targetType == Integer.class) {
            return Integer.parseInt(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + targetType);
        }
    }

    // Method to get the column type map (optional, for external access)
    public static Map<String, Class<?>> getColumnTypeMap() {
        return COLUMN_TYPE_MAP;
    }


}
