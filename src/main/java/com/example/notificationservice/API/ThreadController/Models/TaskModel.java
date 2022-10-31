package com.example.notificationservice.API.ThreadController.Models;


import com.example.notificationservice.API.Models.NotificationType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "task")
public class TaskModel {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "data")
    private String data;

    @Column(name = "planedDate")
    private Date planedDate;

    public TaskModel() {
    }

    public TaskModel(NotificationType type, String data, Date planedDate) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.data = data;
        this.planedDate = planedDate;
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

    public Date getPlanedDate() {
        return planedDate;
    }

    public void setPlanedDate(Date planedDate) {
        this.planedDate = planedDate;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", planedDate=" + planedDate +
                '}';
    }
}
