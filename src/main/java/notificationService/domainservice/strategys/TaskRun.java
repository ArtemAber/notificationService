package notificationService.domainservice.strategys;

import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationResultModel;
import notificationService.domain.notification.NotificationType;

public interface TaskRun {

    public NotificationResultModel run (NotificationModel notificationModel);
    public NotificationType getType();

}
