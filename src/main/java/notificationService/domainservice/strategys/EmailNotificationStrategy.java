package notificationService.domainservice.strategys;

import notificationService.domain.answers.SuccessResultModel;
import notificationService.domain.email.EmailModel;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationResultModel;
import notificationService.domain.notification.NotificationType;
import notificationService.domainservice.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class EmailNotificationStrategy implements TaskRun {

    @Inject
    private EmailService emailService;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;


    @Override
    public NotificationResultModel run(NotificationModel notificationModel) {

        ObjectMapper objectMapper = mapperBuilder.build();

        EmailModel model;

        try {
            model = objectMapper.readValue(notificationModel.getData(), EmailModel.class);
        } catch (JsonProcessingException e) {
             return new NotificationResultModel("ERROR_PARSING_EMAIL_MODEL", "Не удалось распарсить модель EMAIL");
        }
        SuccessResultModel successResultModel = emailService.sendMailAsync(model);

        if (successResultModel.isSuccess()) {
            return new NotificationResultModel();
        } else {
            return new NotificationResultModel(successResultModel.getErrorCode(), successResultModel.getErrorMessage());
        }
    }

    @Override
    public NotificationType getType() {
        return NotificationType.NOTIFICATION_EMAIL;
    }
}
