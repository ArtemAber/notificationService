package notificationService.domainservice;

import notificationService.domain.answers.GuidResultModel;
import notificationService.domain.answers.SuccessResultModel;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationType;
import notificationService.domain.notification.StatusType;
import notificationService.infrastructure.database.NotificationDAO;
import notificationService.domain.sms.SMSAsyncModel;
import notificationService.domain.sms.SMSModel;
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

    @Value("${spring.sms.service}")
    private String smsService;


    public GuidResultModel saveSMS(SMSModel smsModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = sendSMS(smsModel);
        if (!successResultModel.isSuccess()) {
            return new GuidResultModel("ERROR_SENDING_SMS", "Не удалось отправить смс");
        }

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_SMS);
        notificationModel.setStatus(StatusType.FINISHED);
        try {
            notificationModel.setData(objectMapper.writeValueAsString(smsModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_SMS_MODEL", "Не удалось распарсить объект");
        }

        return notificationDAO.saveNotification(notificationModel);
    }

    public SuccessResultModel sendSMS(SMSModel smsModel) {
        StringBuilder otvet = new StringBuilder();
        try {
            URL url = new URL(smsService + login + "&psw=" + password + "&phones=" + smsModel.getNumbers() +
                    "&mes=" + smsModel.getMessage());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                otvet.append(inputLine);
            }
            return new SuccessResultModel();
        } catch (Exception e) {
            return new SuccessResultModel("ERROR_SENDING_SMS", otvet.toString());
        }
    }

    public GuidResultModel saveAsyncSMS(SMSAsyncModel smsAsyncModel) {
        ObjectMapper objectMapper = mapperBuilder.build();

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
