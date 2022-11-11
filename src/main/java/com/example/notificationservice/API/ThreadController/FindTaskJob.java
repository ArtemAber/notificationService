package com.example.notificationservice.API.ThreadController;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.StatusType;
import com.example.notificationservice.API.ThreadController.Models.NotificationResultModel;
import com.example.notificationservice.API.ThreadController.Models.TaskNotificationExecutor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnTransformers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class FindTaskJob {

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    TaskNotificationExecutor taskNotificationExecutor;


    @Scheduled(initialDelay = 10000, fixedRateString = "10000")
    public void findTask() {
        List<NotificationModel> notificationModelList = notificationDAO.getUnfinishedTasks("notificationService");
        if (notificationModelList != null) {
            for (NotificationModel notificationModel: notificationModelList) {
                    CompletableFuture.runAsync(() -> {
                        notificationDAO.updateStatus(notificationModel.getId(), StatusType.PROCESSING);
                        NotificationResultModel notificationModel1 = taskNotificationExecutor.runTask(notificationModel);
                        updateTask(notificationModel.getId(), notificationModel.getAttempts() + 1, notificationModel1);
                    });

//                NotificationResultModel notificationResultModel = taskNotificationExecutor.runTask(notificationModel);
//
//                updateTask(notificationModel.getId(), notificationModel.getAttempts() + 1, notificationResultModel);
            }
        }
    }

    private void updateTask(UUID id, int attempts, NotificationResultModel notificationResultModel) {
        notificationDAO.updateNotificationEntity(id, attempts, notificationResultModel);
    }
}

