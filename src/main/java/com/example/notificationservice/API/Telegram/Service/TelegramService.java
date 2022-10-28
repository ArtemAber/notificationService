package com.example.notificationservice.API.Telegram.Service;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Models.StatusType;
import com.example.notificationservice.API.Telegram.Models.TelegramBot;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TelegramService {

    private final TelegramBot telegramBot;
    private NotificationDAO notificationDao;

    @Autowired
    public TelegramService(TelegramBot telegramBot, NotificationDAO notificationDao) {
        this.telegramBot = telegramBot;
        this.notificationDao = notificationDao;
    }

    public void saveMessage(TelegramModel telegramModel, NotificationType type) {
        NotificationModel notificationModel = new NotificationModel(type, telegramModel.toString());
        notificationModel.setCreatedAt(new Date());
        notificationModel.setStatus(StatusType.PROCESSING);

        telegramBot.sendMes(telegramModel, notificationModel);
    }
}
