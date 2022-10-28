package com.example.notificationservice.API.Telegram.Models;

import com.example.notificationservice.API.Hibernate.service.NotificationHibernateService;
import com.example.notificationservice.API.Models.FileModel;
import com.example.notificationservice.API.Models.NotificationModel;
import com.example.notificationservice.API.Models.PictureModel;
import com.example.notificationservice.API.Models.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Date;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    private String recipient;

    private TelegramModel telegramModel;

    private final NotificationHibernateService notificationHibernateService;

    @Autowired
    public TelegramBot(NotificationHibernateService notificationHibernateService) {
        this.notificationHibernateService = notificationHibernateService;
    }


    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String message = update.getMessage().getText().trim();
                recipient = update.getMessage().getChatId().toString();

                System.out.println(recipient);

                SendMessage sendMessage = new SendMessage();

                sendMessage.setChatId(recipient);
                sendMessage.setText(message);
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMes(TelegramModel telegramModel, NotificationModel notificationModel) {
        this.recipient = telegramModel.getChatId();
        this.telegramModel = telegramModel;

        if (telegramModel != null) {
            if (telegramModel.getMessage() != null) {
                sendMessage(telegramModel, notificationModel);
            }
            if (telegramModel.getFiles() != null) {
                notificationModel.setStatus(sendMsgWithFiles(telegramModel, notificationModel));
            }
            if (telegramModel.getPictures() != null) {
                notificationModel.setStatus(sendPhoto(telegramModel, notificationModel));
            }
        }
        notificationModel.setSendDate(new Date());
        notificationHibernateService.saveNotification(notificationModel);
    }

    private void sendMessage(TelegramModel telegramModel, NotificationModel notificationModel) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(telegramModel.getChatId());
        sendMessage.setText(telegramModel.getMessage());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private StatusType sendMsgWithFiles(TelegramModel telegramModel, NotificationModel notificationModel) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(recipient);

        for (FileModel fileModel: telegramModel.getFiles()) {
            sendDocument.setDocument(new InputFile(getFile(fileModel)));
            try {
                execute(sendDocument);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                notificationModel.setMessage(e.getMessage());
                return StatusType.FAILED;
            }
        }
        return StatusType.FINISHED;
    }

    public File getFile(FileModel fileModel) {

        FileOutputStream fos;
        File file = new File(fileModel.getName());
        try {
            fos = new FileOutputStream(file);
            fos.write(new String(Base64.getDecoder().decode(fileModel.getData())).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public StatusType sendPhoto(TelegramModel telegramModel, NotificationModel notificationModel) {
        File image = null;
        try {
            image = new File(this.getClass().getClassLoader().getResource("test.jpg").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            notificationModel.setMessage(e.getMessage());
            return StatusType.FAILED;
        }
        FileOutputStream fos;
        try {
            for(PictureModel pictureModel: telegramModel.getPictures()) {
                fos = new FileOutputStream(image);
                byte[] imgByte = Base64.getDecoder().decode(pictureModel.getData());
                fos.write(imgByte);
                fos.close();
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(recipient);
                sendPhoto.setPhoto(new InputFile(image));
                execute(sendPhoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            notificationModel.setMessage(e.getMessage());
            return StatusType.FAILED;
        }
        return StatusType.FINISHED;
    }
}
