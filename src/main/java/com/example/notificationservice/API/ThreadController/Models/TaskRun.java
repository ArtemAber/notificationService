package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;

public interface TaskRun {

    public NotificationResultModel run (NotificationModel notificationModel);
    public NotificationType getType();

}
