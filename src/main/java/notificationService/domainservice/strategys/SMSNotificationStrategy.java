package notificationService.domainservice.strategys;

import notificationService.domain.answers.SuccessResultModel;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationResultModel;
import notificationService.domain.notification.NotificationType;
import notificationService.domain.sms.SMSModel;
import notificationService.domainservice.SMSService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SMSNotificationStrategy implements TaskRun {

    @Inject
    private SMSService smsService;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;


    @Override
    public NotificationResultModel run(NotificationModel notificationModel) {

        ObjectMapper objectMapper = mapperBuilder.build();

        SMSModel smsModel;

        try {
            smsModel = objectMapper.readValue(notificationModel.getData(), SMSModel.class);
        } catch (JsonProcessingException e) {
            return new NotificationResultModel("ERROR_PARSING_SMS_MODEL", "Не удалось распарсить модель SMS");
        }
        SuccessResultModel successResultModel = smsService.sendSMS(smsModel);

        if (successResultModel.isSuccess()) {
            return new NotificationResultModel();
        } else {
            return new NotificationResultModel(successResultModel.getErrorCode(), successResultModel.getErrorMessage());
        }
    }

    @Override
    public NotificationType getType() {
        return NotificationType.NOTIFICATION_SMS;
    }
}
