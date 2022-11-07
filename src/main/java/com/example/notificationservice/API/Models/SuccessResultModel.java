package com.example.notificationservice.API.Models;

public class SuccessResultModel {

    private boolean success;
    private String errorCode;
    private String errorMessage;

    public SuccessResultModel() {
        this.success = true;
        this.errorCode = "";
        this.errorMessage = "";
    }

    public SuccessResultModel(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
