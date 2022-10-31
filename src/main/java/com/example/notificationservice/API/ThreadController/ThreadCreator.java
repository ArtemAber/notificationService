package com.example.notificationservice.API.ThreadController;

import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Email.Service.EmailService;
import com.example.notificationservice.API.Hibernate.service.TaskHibernateService;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.example.notificationservice.API.SMS.Service.SMSService;
import com.example.notificationservice.API.Telegram.Service.TelegramService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Service
public class ThreadCreator {

    private final EmailService emailService;
    private final SMSService smsService;
    private final TelegramService telegramService;
    private final ModelMapper modelMapper;
    private final TaskHibernateService taskHibernateService;

    public ThreadCreator(EmailService emailService, SMSService smsService, TelegramService telegramService, ModelMapper modelMapper, TaskHibernateService taskHibernateService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.telegramService = telegramService;
        this.modelMapper = modelMapper;
        this.taskHibernateService = taskHibernateService;
    }

    public String threadCreate(SMSModel smsModel) throws Exception{

        Callable task = () -> {
            return smsService.saveSMS(smsModel, NotificationType.NOTIFICATION_SMS);
        };

        FutureTask<String> future = new FutureTask<>(task);

        new Thread(future).start();
        return future.get();
    }
}
