package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Email.Service.EmailService;
import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Models.SuccessResultModel;
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
    public SuccessResultModel run(NotificationModel notificationModel) {

        ObjectMapper objectMapper = mapperBuilder.build();

        EmailModel model;

        try {
            model = objectMapper.readValue(notificationModel.getData(), EmailModel.class);
        } catch (JsonProcessingException e) {
             return new SuccessResultModel("ERROR_PARSING_EMAIL_MODEL", "Не удалось распарсить модель EMAIL");
        }
        return emailService.sendMailAsync(model);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.NOTIFICATION_EMAIL;
    }
}
