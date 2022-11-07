package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Models.SuccessResultModel;
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

    @Override
    public SuccessResultModel run(NotificationModel notificationModel) {

        ObjectMapper objectMapper = mapperBuilder.build();

        TelegramModel telegramModel;

        try {
            telegramModel = objectMapper.readValue(notificationModel.getData(), TelegramModel.class);
        } catch (JsonProcessingException e) {
            return new SuccessResultModel("ERROR_PARSING_TELEGRAM_MODEL", "Не удалость распарсить модель TELEGRAM");
        }
        return telegramBot.sendTelegramAsync(telegramModel);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.NOTIFICATION_TELEGRAM;
    }
}
