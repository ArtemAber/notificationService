package com.example.notificationservice.API.ThreadController.Models;

import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Models.SuccessResultModel;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.example.notificationservice.API.SMS.Service.SMSService;
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
    public SuccessResultModel run(NotificationModel notificationModel) {

        ObjectMapper objectMapper = mapperBuilder.build();

        SMSModel smsModel;

        try {
            smsModel = objectMapper.readValue(notificationModel.getData(), SMSModel.class);
        } catch (JsonProcessingException e) {
            return new SuccessResultModel("ERROR_PARSING_SMS_MODEL", "Не удалось распарсить модель SMS");
        }
        return smsService.sendSMS(smsModel);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.NOTIFICATION_SMS;
    }
}
