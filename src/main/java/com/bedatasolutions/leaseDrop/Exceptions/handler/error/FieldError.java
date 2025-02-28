package com.bedatasolutions.leaseDrop.Exceptions.handler.error;


public class FieldError {

    private String field;
    private String errorCode;
    private String errorMessage;

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
