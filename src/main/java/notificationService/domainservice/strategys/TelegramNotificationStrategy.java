package notificationService.domainservice.strategys;

import notificationService.domain.answers.SuccessResultModel;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationResultModel;
import notificationService.domain.notification.NotificationType;
import notificationService.domain.telegram.PartsModel;
import notificationService.infrastructure.database.NotificationDAO;
import notificationService.domainservice.TelegramBot;
import notificationService.domain.telegram.TelegramModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class TelegramNotificationStrategy implements TaskRun {

    @Inject
    private TelegramBot telegramBot;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Inject
    private NotificationDAO notificationDAO;

    @Override
    public NotificationResultModel run(NotificationModel notificationModel) {

        ObjectMapper objectMapper = mapperBuilder.build();
        TelegramModel telegramModel;
        PartsModel partsModel;
        SuccessResultModel finalResult = new SuccessResultModel();

        try {
            telegramModel = objectMapper.readValue(notificationModel.getData(), TelegramModel.class);
        } catch (JsonProcessingException e) {
            return new NotificationResultModel("ERROR_PARSING_TELEGRAM_MODEL", "Не удалость распарсить модель TELEGRAM");
        }
        try {
            partsModel = objectMapper.readValue(notificationModel.getParts(), PartsModel.class);
        } catch (JsonProcessingException e) {
            return new NotificationResultModel("ERROR_PARSING_PARTS", "Не удалость распарсить модель PARTS");
        }


        if (!partsModel.shippedMessage()){
            SuccessResultModel result = telegramBot.sendMessage(telegramModel);
            if (result.isSuccess()) {
                notificationDAO.successfulSendingMessage(notificationModel.getId());
            } else {
                finalResult.setSuccess(false);
                finalResult.setErrorCode((finalResult.getErrorCode()!=null ? finalResult.getErrorCode() : "") + result.getErrorCode());
                finalResult.setErrorMessage((finalResult.getErrorMessage()!=null ? finalResult.getErrorMessage() : "") + result.getErrorMessage());
            }
        }
        if (!partsModel.shippedFiles()){
            SuccessResultModel result = telegramBot.sendMsgWithFiles(telegramModel);
            if (result.isSuccess()) {
                notificationDAO.successfulSendingFiles(notificationModel.getId());
            } else {
                finalResult.setSuccess(false);
                finalResult.setErrorCode((finalResult.getErrorCode()!=null ? finalResult.getErrorCode() : "") + result.getErrorCode());
                finalResult.setErrorMessage((finalResult.getErrorMessage()!=null ? finalResult.getErrorMessage() : "") + result.getErrorMessage());
            }
        }
        if (!partsModel.shippedPictures()){
            SuccessResultModel result = telegramBot.sendPhoto(telegramModel);
            if (result.isSuccess()) {
                notificationDAO.successfulSendingPicture(notificationModel.getId());
            } else {
                finalResult.setSuccess(false);
                finalResult.setErrorCode((finalResult.getErrorCode()!=null ? finalResult.getErrorCode() : "") + result.getErrorCode());
                finalResult.setErrorMessage((finalResult.getErrorMessage()!=null ? finalResult.getErrorMessage() : "") + result.getErrorMessage());
            }
        }


        if (finalResult.isSuccess()){
            return new NotificationResultModel();
        } else {
            return new NotificationResultModel (finalResult.getErrorCode(), finalResult.getErrorMessage());
        }

    }

    @Override
    public NotificationType getType() {
        return NotificationType.NOTIFICATION_TELEGRAM;
    }
}
