package com.example.notificationservice.API.Models;

public class NotificationAddModel {

    private NotificationType type;

    private String data;


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
}
