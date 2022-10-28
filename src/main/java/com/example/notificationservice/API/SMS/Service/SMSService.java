package com.example.notificationservice.API.SMS.Service;

import com.example.notificationservice.API.Hibernate.service.NotificationHibernateService;
import com.example.notificationservice.API.Models.*;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Service
public class SMSService {

    private final NotificationHibernateService notificationHibernateService;

    @Value("${sms.login}")
    private String login;

    @Value("${sms.password}")
    private String password;


    public SMSService(NotificationHibernateService notificationHibernateService) {
        this.notificationHibernateService = notificationHibernateService;
    }

    public String saveSMS(SMSModel smsModel, NotificationType type) {
        NotificationModel notificationModel = new NotificationModel(type, smsModel.toString());
        notificationModel.setCreatedAt(new Date());

        notificationModel.setStatus(StatusType.PROCESSING);

        return sendSMS(smsModel, notificationModel);
    }

    public String sendSMS(SMSModel smsModel, NotificationModel notificationModel) {
        String answer;

        try {
            URL url = new URL("https://smsc.ru/sys/send.php?login=" + login + "&psw=" + password + "&phones=" + smsModel.getNumbers() +
                    "&mes=" + smsModel.getMessage());
            System.out.println(url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuilder otvet = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                otvet.append(inputLine);
            }
            answer = otvet.toString();

            notificationModel.setSendDate(new Date());
            notificationModel.setStatus(StatusType.FINISHED);
            notificationModel.setMessage(otvet.toString());
            notificationHibernateService.saveNotification(notificationModel);
        } catch (Exception e) {
            notificationModel.setStatus(StatusType.FAILED);
            notificationModel.setMessage(e.getMessage());
            notificationHibernateService.saveNotification(notificationModel);
            answer = "ошибка отправки";
        }
        return answer;
    }
}
