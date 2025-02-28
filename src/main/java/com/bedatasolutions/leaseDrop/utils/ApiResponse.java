package com.bedatasolutions.leaseDrop.utils;

import com.bedatasolutions.leaseDrop.constants.db.ResponseStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> {
    private String message;
    private T data;
    private String status;

    // Status Codes
    // OK
    // ERROR
    // Challenge Required

    public ApiResponse(String message, T data, String status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(message, data, ResponseStatus.SUCCESS.toString()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, T data) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(message, data, ResponseStatus.FAILED.toString()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> challengeRequired(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(message, data, ResponseStatus.CHALLENGE_REQUIRED.toString()));
    }

    public  static <T> ResponseEntity<ApiResponse<T>> notFound(String message, T data) {
        return ResponseEntity.status(404).body(new ApiResponse<>(message, data, ResponseStatus.NOT_FOUND.toString()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message, T data) {
        return ResponseEntity.status(403).body(new ApiResponse<>(message, data, ResponseStatus.FORBIDDEN.toString()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message, T data) {
        return ResponseEntity.status(401).body(new ApiResponse<>(message, data, ResponseStatus.UNAUTHORIZED.toString()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, T data) {
        return ResponseEntity.status(500).body(new ApiResponse<>(message, data, ResponseStatus.INTERNAL_SERVER_ERROR.toString()));
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
}
