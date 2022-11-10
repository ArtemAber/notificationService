package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskNotificationExecutor {

    private static final Map<NotificationType, TaskRun> STRATEGY_MAP = new HashMap<>();

    public TaskNotificationExecutor(List<TaskRun> taskRuns) {
        taskRuns.forEach(strategy -> STRATEGY_MAP.put(strategy.getType(), strategy));
    }

    public NotificationResultModel runTask(NotificationModel notificationModel) {
        var strategy = STRATEGY_MAP.get(notificationModel.getType());

        if (strategy == null) {
            throw new UnsupportedOperationException("Task with type: " + notificationModel.getType() + " not supported");
        }

        return strategy.run(notificationModel);
    }
}
