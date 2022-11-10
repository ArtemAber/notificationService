package com.example.notificationservice.API.Models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NotificationModel {

    private UUID id;

    private NotificationType type;

    private String data;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdate;

    private StatusType status;

    private String message;

    private int attempts;

    private PartsModel partsModel;

    private LocalDateTime plannedDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public PartsModel getPartsModel() {
        return partsModel;
    }

    public void setPartsModel(PartsModel partsModel) {
        this.partsModel = partsModel;
    }

    public LocalDateTime getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDateTime plannedDate) {
        this.plannedDate = plannedDate;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdate=" + lastUpdate +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", attempts=" + attempts +
                ", partsModel=" + partsModel +
                ", plannedDate=" + plannedDate +
                '}';
    }
}