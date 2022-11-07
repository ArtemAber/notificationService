package com.example.notificationservice.API.ThreadController;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.StatusType;
import com.example.notificationservice.API.Models.SuccessResultModel;
import com.example.notificationservice.API.ThreadController.Models.TaskNotificationExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class FindTaskJob {

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    TaskNotificationExecutor taskNotificationExecutor;


    @Scheduled(initialDelay = 10000, fixedRateString = "10000")
    public void findTask() {
        if (notificationDAO.getUnfinishedTasks() != null) {
            for (NotificationModel notificationModel: notificationDAO.getUnfinishedTasks()) {
                    updateTask(taskNotificationExecutor.runTask(notificationModel), notificationModel);
            }
        }
    }

    private void updateTask(SuccessResultModel successResultModel, NotificationModel notificationModel) {
        if (successResultModel.isSuccess()) {
            notificationModel.setStatus(StatusType.FINISHED);
        } else {
            notificationModel.setStatus(StatusType.FAILED);
            notificationModel.setMessage(successResultModel.getErrorMessage());
        }
        notificationDAO.updateNotificationEntity(notificationModel);
    }
}

