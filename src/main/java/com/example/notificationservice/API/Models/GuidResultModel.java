package com.example.notificationservice.API.Models;

import java.util.UUID;

public class GuidResultModel {

    private UUID id;

    private String errorCode;

    private String errorMessage;

    public GuidResultModel(UUID id) {
        this.id = id;
    }

    public GuidResultModel(UUID id, String errorCode, String errorMessage) {
        this.id = id;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
