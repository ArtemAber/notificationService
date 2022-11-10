package com.example.notificationservice.API.SMS.Service;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.*;
import com.example.notificationservice.API.SMS.Models.SMSAsyncModel;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class SMSService {

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Value("${sms.login}")
    private String login;

    @Value("${sms.password}")
    private String password;


    public GuidResultModel saveSMS(SMSModel smsModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = sendSMS(smsModel);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_SMS);
        try {
            notificationModel.setData(objectMapper.writeValueAsString(smsModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_SMS_MODEL", "Не удалось распарсить объект");
        }

        if (!successResultModel.isSuccess()) {
            notificationModel.setStatus(StatusType.FAILED);
            notificationModel.setMessage(successResultModel.getErrorCode() + successResultModel.getErrorMessage());
        } else {
            notificationModel.setStatus(StatusType.FINISHED);
        }
        return notificationDAO.saveNotification(notificationModel);
    }

    public SuccessResultModel sendSMS(SMSModel smsModel) {
        try {
            URL url = new URL("https://smsc.ru/sys/send.php?login=" + login + "&psw=" + password + "&phones=" + smsModel.getNumbers() +
                    "&mes=" + smsModel.getMessage());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuilder otvet = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                otvet.append(inputLine);
            }
            return new SuccessResultModel();
        } catch (Exception e) {
            return new SuccessResultModel("ERROR_SENDING_SMS", "Не удалось отправить смс");
        }
    }

    public GuidResultModel saveAsyncSMS(SMSAsyncModel smsAsyncModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = new SuccessResultModel();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_SMS);
        notificationModel.setPlannedDate(smsAsyncModel.getAsync());
        try {
            notificationModel.setData(objectMapper.writeValueAsString(smsAsyncModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_ASYNC_SMS_MODEL", "Не удалось распарсить объект");
        }
        notificationModel.setStatus(StatusType.INIT);
        notificationModel.setAttempts(0);

        return notificationDAO.saveNotification(notificationModel);
    }
}
