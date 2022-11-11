package com.example.notificationservice.API.Email.Service;

import com.example.notificationservice.API.Email.Models.EmailAsyncModel;
import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Inject
    private JavaMailSender javaMailSender;

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Value("${spring.mail.username}")
    private String sender;

    public GuidResultModel sendMail(EmailModel emailModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = new SuccessResultModel();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_EMAIL);
        try {
            notificationModel.setData(objectMapper.writeValueAsString(emailModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_EMAIL_MODEL", "Не удалось распарсить объект");
        }

        if (emailModel.getPictures() == null && emailModel.getFiles() == null) {
            successResultModel = this.sendSimpleMail(emailModel);
        } else {
            successResultModel = this.sendMailWithAttachment(emailModel);
        }

        if (!successResultModel.isSuccess()) {
            return new GuidResultModel("ERROR_SENDING_MAIL", "Не удалось отправить письмо");
        } else {
            notificationModel.setStatus(StatusType.FINISHED);
        }
        return notificationDAO.saveNotification(notificationModel);
    }


    public SuccessResultModel sendMailAsync(EmailModel emailModel) {
        if (emailModel.getPictures() == null && emailModel.getFiles() == null) {
            return this.sendSimpleMail(emailModel);
        } else {
            return this.sendMailWithAttachment(emailModel);
        }
    }

    public GuidResultModel sendAdAsync(EmailAsyncModel emailAsyncModel) {
        ObjectMapper objectMapper = mapperBuilder.build();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_EMAIL);
        notificationModel.setPlannedDate(emailAsyncModel.getAsync());
        try {
            notificationModel.setData(objectMapper.writeValueAsString(emailAsyncModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_ASYNC_EMAIL_MODEL", "Не удалось распарсить объект");
        }
        notificationModel.setStatus(StatusType.INIT);
        notificationModel.setAttempts(0);

        return notificationDAO.saveNotification(notificationModel);
    }

    public SuccessResultModel sendMailWithAttachment(EmailModel emailModel) {
        try {
            MimeMessage mailMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setFrom(this.sender);
            messageHelper.setTo(emailModel.getEmail());
            messageHelper.setSubject(emailModel.getTitle());
            messageHelper.setText(emailModel.getDescription());
            if (emailModel.getPictures() != null) {
                for (PictureModel pictureModel: emailModel.getPictures()) {
                    messageHelper.setText("<html><body><img src=\"data:image/png;base64," + pictureModel.getData() + "\"></body></html>", true);
                }
            }

            if (emailModel.getFiles() != null) {
                for(FileModel fileModel: emailModel.getFiles()) {
                    File file = new File(fileModel.getName());
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write((new String(Base64.getDecoder().decode(fileModel.getData())).getBytes()));
                    fos.close();
                    messageHelper.addAttachment(fileModel.getName(), file);
                }
            }


            this.javaMailSender.send(mailMessage);

            return new SuccessResultModel();
        } catch (Exception e) {
            return new SuccessResultModel("ERROR_SENDING_MAIL_WITH_ATTACHMENT", "Не удалось отправить сообщение с вложениями");
        }
    }

    public SuccessResultModel sendSimpleMail(EmailModel emailModel) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(this.sender);
            mailMessage.setTo(emailModel.getEmail());
            mailMessage.setSubject(emailModel.getTitle());
            mailMessage.setText(emailModel.getDescription());
            this.javaMailSender.send(mailMessage);

            return new SuccessResultModel();
        } catch (Exception e) {
            return new SuccessResultModel("ERROR_SENDING_SIMPLE_MAIL.", "Не удалось отправить простое сообщение");
        }
    }
}
