package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Models.SuccessResultModel;

public interface TaskRun {

    public SuccessResultModel run (NotificationModel notificationModel);
    public NotificationType getType();

}
