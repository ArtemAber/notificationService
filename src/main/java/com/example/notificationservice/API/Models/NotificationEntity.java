package com.example.notificationservice.API.Models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "data")
    private String data;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "send_date")
    private LocalDateTime lastUpdate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Column(name = "message")
    private String message;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "parts")
    private String parts;

    @Column(name = "planned_date")
    private LocalDateTime plannedDate;

    @Column(name = "service_name")
    private String serviceName;

    public NotificationEntity() {
    }

    public NotificationEntity(NotificationType type, String data) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.data = data;
        this.status = StatusType.INIT;
    }

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

    public void setLastUpdate(LocalDateTime sendDate) {
        this.lastUpdate = sendDate;
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

    public String getParts() {
        return parts;
    }

    public void setParts(String partsModel) {
        this.parts = partsModel;
    }

    public LocalDateTime getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDateTime plannedDate) {
        this.plannedDate = plannedDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "NotificationEntity{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdate=" + lastUpdate +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", attempts=" + attempts +
                ", partsModel='" + parts + '\'' +
                ", plannedDate=" + plannedDate +
                '}';
    }
}
