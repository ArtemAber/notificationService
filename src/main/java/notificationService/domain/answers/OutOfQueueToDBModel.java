package notificationService.domain.answers;

import notificationService.domain.notification.NotificationResultModel;

import java.util.UUID;

public class OutOfQueueToDBModel {

    private UUID id;

    private int attempts;

    private NotificationResultModel notificationResultModel;

    public OutOfQueueToDBModel() {

    }

    public OutOfQueueToDBModel(UUID id, int attempts, NotificationResultModel notificationResultModel) {
        this.id = id;
        this.attempts = attempts;
        this.notificationResultModel = notificationResultModel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public NotificationResultModel getNotificationResultModel() {
        return notificationResultModel;
    }

    public void setNotificationResultModel(NotificationResultModel notificationResultModel) {
        this.notificationResultModel = notificationResultModel;
    }
}
