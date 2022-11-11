package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.*;
import com.example.notificationservice.API.Telegram.Models.TelegramBot;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
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


        if (Boolean.FALSE.equals(partsModel.isSendMessage())){
            SuccessResultModel result = telegramBot.sendMessage(telegramModel);
            if (result.isSuccess()) {
                notificationDAO.successfulSendingMessage(notificationModel.getId());
            } else {
                finalResult.setSuccess(false);
                finalResult.setErrorCode(finalResult.getErrorCode() + result.getErrorCode());
                finalResult.setErrorMessage(finalResult.getErrorMessage() + result.getErrorMessage());
            }
        }
        if (Boolean.FALSE.equals(partsModel.isSendFiles())){
            SuccessResultModel result = telegramBot.sendMsgWithFiles(telegramModel);
            if (result.isSuccess()) {
                notificationDAO.successfulSendingFiles(notificationModel.getId());
            } else {
                finalResult.setSuccess(false);
                finalResult.setErrorCode(finalResult.getErrorCode() + result.getErrorCode());
                finalResult.setErrorMessage(finalResult.getErrorMessage() + result.getErrorMessage());
            }
        }
        if (Boolean.FALSE.equals(partsModel.isSendPictures())){
            SuccessResultModel result = telegramBot.sendPhoto(telegramModel);
            if (result.isSuccess()) {
                notificationDAO.successfulSendingPicture(notificationModel.getId());
            } else {
                finalResult.setSuccess(false);
                finalResult.setErrorCode(finalResult.getErrorCode() + result.getErrorCode());
                finalResult.setErrorMessage(finalResult.getErrorMessage() + result.getErrorMessage());
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
