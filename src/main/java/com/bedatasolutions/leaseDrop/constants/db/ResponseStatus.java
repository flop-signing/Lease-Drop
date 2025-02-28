package com.bedatasolutions.leaseDrop.constants.db;

public enum ResponseStatus {
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    NOT_FOUND("NOT_FOUND"),
    UNAUTHORIZED("UNAUTHORIZED"),
    CHALLENGE_REQUIRED("CHALLENGE_REQUIRED"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    FORBIDDEN("FORBIDDEN"),
    BAD_REQUEST("BAD_REQUEST");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
