package com.example.notificationservice.API.ThreadController;

import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Email.Service.EmailService;
import com.example.notificationservice.API.Hibernate.service.TaskHibernateService;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.example.notificationservice.API.SMS.Service.SMSService;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
import com.example.notificationservice.API.Telegram.Service.TelegramService;
import com.example.notificationservice.API.ThreadController.Models.TaskModel;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class FindTaskJob {

    private final TaskHibernateService taskHibernateService;
    private final TelegramService telegramService;
    private final EmailService emailService;
    private final SMSService smsService;

    public FindTaskJob(TaskHibernateService taskHibernateService, TelegramService telegramService, EmailService emailService, SMSService smsService) {
        this.taskHibernateService = taskHibernateService;
        this.telegramService = telegramService;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Scheduled(initialDelay = 10000, fixedRateString = "10000")
    public void findTask() {
        if (taskHibernateService.getAllTask() != null) {
            for (TaskModel taskModel : taskHibernateService.getAllTask()) {
                if (taskModel.getPlanedDate().before(new Date())) {
                    if (taskModel.getType() == NotificationType.NOTIFICATION_TELEGRAM) {
                        executeTelegramTask(taskModel);
                    } else if (taskModel.getType() == NotificationType.NOTIFICATION_EMAIL) {
                        executeEmailTask(taskModel);
                    } else if (taskModel.getType() == NotificationType.NOTIFICATION_SMS) {
                        executeSMSTask(taskModel);
                    }
                }
            }
        }
    }

    private void executeTelegramTask(TaskModel taskModel) {
        Runnable task = () -> {
            try {
                telegramService.saveMessage(new TelegramModel(taskModel.getData()), taskModel.getType());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            taskHibernateService.deleteTask(taskModel);
        };
        new Thread(task).start();
    }

    private void executeEmailTask(TaskModel taskModel) {
        Runnable task = () -> {
            try {
                emailService.saveMail(new EmailModel(taskModel.getData()), NotificationType.NOTIFICATION_EMAIL);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            taskHibernateService.deleteTask(taskModel);
        };
        new Thread(task).start();
    }

    private void executeSMSTask(TaskModel taskModel) {
        Runnable task = () -> {
            try {
                smsService.saveSMS(new SMSModel(taskModel.getData()), NotificationType.NOTIFICATION_SMS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            taskHibernateService.deleteTask(taskModel);
        };
        new Thread(task).start();
    }
}

