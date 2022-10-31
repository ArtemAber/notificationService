package com.example.notificationservice.API.Email.Service;

import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Hibernate.service.NotificationHibernateService;
import com.example.notificationservice.API.Models.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Date;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final NotificationHibernateService notificationHibernateService;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, NotificationHibernateService notificationHibernateService) {
        this.javaMailSender = javaMailSender;
        this.notificationHibernateService = notificationHibernateService;
    }

    public String saveMail(EmailModel emailModel, NotificationType type) {
        NotificationModel notificationModel = new NotificationModel(type, emailModel.toString());
        notificationModel.setCreatedAt(new Date());
        notificationModel.setStatus(StatusType.PROCESSING);
        return emailModel.getPictures() == null && emailModel.getFiles() == null ? this.sendSimpleMail(emailModel, notificationModel) : this.sendMailWithAttachment(emailModel, notificationModel);
    }

    public String sendMailWithAttachment(EmailModel emailModel, NotificationModel notificationModel) {
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
            notificationModel.setSendDate(new Date());
            notificationModel.setStatus(StatusType.FINISHED);
            notificationHibernateService.saveNotification(notificationModel);
            return "Сообщение с вложением отправлено на почту " + emailModel.getEmail();
        } catch (Exception e) {
            notificationModel.setStatus(StatusType.FAILED);
            notificationModel.setMessage(e.getMessage());
            notificationHibernateService.saveNotification(notificationModel);
            return "Сообщение с вложением не отправлено на почту " + emailModel.getEmail();
        }
    }

    public String sendSimpleMail(EmailModel emailModel, NotificationModel notificationModel) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(this.sender);
            mailMessage.setTo(emailModel.getEmail());
            mailMessage.setSubject(emailModel.getTitle());
            mailMessage.setText(emailModel.getDescription());
            this.javaMailSender.send(mailMessage);
            notificationModel.setSendDate(new Date());
            notificationModel.setStatus(StatusType.FINISHED);
            notificationHibernateService.saveNotification(notificationModel);
            return "Обычное сообщение отправлено на почту " + emailModel.getEmail();
        } catch (Exception e) {
            notificationModel.setStatus(StatusType.FAILED);
            notificationModel.setMessage(e.getMessage());
            notificationHibernateService.saveNotification(notificationModel);
            return "Обычное сообщение не отправлено на почту " + emailModel.getEmail();
        }
    }
}
