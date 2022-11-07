package com.example.notificationservice.API.Telegram.Models;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Base64;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    private String recipient;

    private TelegramModel telegramModel;

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    Jackson2ObjectMapperBuilder mapperBuilder;


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

    public GuidResultModel sendMes(TelegramModel telegramModel) {
        this.recipient = telegramModel.getChatId();
        this.telegramModel = telegramModel;

        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = new SuccessResultModel();
        if (telegramModel != null) {
            if (telegramModel.getMessage() != null) {
                SuccessResultModel res = sendMessage(telegramModel);
                if (!res.isSuccess()) {
                    successResultModel.setSuccess(false);
                    successResultModel.setErrorCode(successResultModel.getErrorCode() + "; " + res.getErrorCode());
                    successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; " + res.getErrorMessage());
                }
            }
            if (telegramModel.getFiles() != null) {
                SuccessResultModel res = sendMsgWithFiles(telegramModel);
                if (!res.isSuccess()) {
                    successResultModel.setSuccess(false);
                    successResultModel.setErrorCode(successResultModel.getErrorCode() + "; " + res.getErrorCode());
                    successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; " + res.getErrorMessage());
                }
            }
            if (telegramModel.getPictures() != null) {
                SuccessResultModel res = sendPhoto(telegramModel);
                if (!res.isSuccess()) {
                    successResultModel.setSuccess(false);
                    successResultModel.setErrorCode(successResultModel.getErrorCode() + "; " + res.getErrorCode());
                    successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; " + res.getErrorMessage());
                }
            }
        }

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_TELEGRAM);
        try {
            notificationModel.setData(objectMapper.writeValueAsString(telegramModel));
        } catch (JsonProcessingException e) {
            successResultModel.setSuccess(false);
            successResultModel.setErrorCode(successResultModel.getErrorCode() + "; ERROR_PARSING_TELEGRAM_MODEL");
            successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; Не удалось распарсить объект");
        }
        if (!successResultModel.isSuccess()) {
            notificationModel.setStatus(StatusType.FAILED);
            notificationModel.setMessage(successResultModel.getErrorMessage());
        } else {
            notificationModel.setStatus(StatusType.FINISHED);
        }
        return notificationDAO.saveNotification(notificationModel, successResultModel);
    }

    public SuccessResultModel sendTelegramAsync(TelegramModel telegramModel) {
        SuccessResultModel successResultModel = new SuccessResultModel();
        if (telegramModel != null) {
            if (telegramModel.getMessage() != null) {
                SuccessResultModel res = sendMessage(telegramModel);
                successResultModel.setSuccess(false);
                successResultModel.setErrorCode(successResultModel.getErrorCode() + "; " + res.getErrorCode());
                successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; " + res.getErrorMessage());
            }
            if (telegramModel.getFiles() != null) {
                SuccessResultModel res = sendMsgWithFiles(telegramModel);
                successResultModel.setSuccess(false);
                successResultModel.setErrorCode(successResultModel.getErrorCode() + "; " + res.getErrorCode());
                successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; " + res.getErrorMessage());
            }
            if (telegramModel.getPictures() != null) {
                SuccessResultModel res = sendPhoto(telegramModel);
                successResultModel.setSuccess(false);
                successResultModel.setErrorCode(successResultModel.getErrorCode() + "; " + res.getErrorCode());
                successResultModel.setErrorMessage(successResultModel.getErrorMessage() + "; " + res.getErrorMessage());
            }
        } else {
            successResultModel = new SuccessResultModel("ERROR_SENDING_TELEGRAM", "Пустые данные");
        }
        return successResultModel;
    }

    public GuidResultModel saveAsyncMes(TelegramAsyncModel telegramAsyncModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = new SuccessResultModel();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_TELEGRAM);
        notificationModel.setPlannedDate(telegramAsyncModel.getAsync());
        try {
            notificationModel.setData(objectMapper.writeValueAsString(telegramAsyncModel));
        } catch (JsonProcessingException e) {
            successResultModel.setSuccess(false);
            successResultModel.setErrorCode("ERROR_PARSING_ASYNC_TELEGRAM_MODEL");
            successResultModel.setErrorMessage("Не удалось распарсить объект");
        }
        notificationModel.setStatus(StatusType.INIT);
        notificationModel.setAttempts(0);

        return notificationDAO.saveNotification(notificationModel, successResultModel);
    }

    private SuccessResultModel sendMessage(TelegramModel telegramModel) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(telegramModel.getChatId());
        sendMessage.setText(telegramModel.getMessage());
        try {
            execute(sendMessage);
            return new SuccessResultModel();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return new SuccessResultModel("ERROR_SENDING_TELEGRAM", "Не удалось отправить текстовое сообщение в телеграм");
        }
    }

    private SuccessResultModel sendMsgWithFiles(TelegramModel telegramModel) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(telegramModel.getChatId());

        for (FileModel fileModel : telegramModel.getFiles()) {
            sendDocument.setDocument(new InputFile(getFile(fileModel)));
            try {
                execute(sendDocument);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return new SuccessResultModel("ERROR_SENDING_TELEGRAM", "Не удалось отправить файлы в телеграм");
            }
        }
        return new SuccessResultModel();
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

    public SuccessResultModel sendPhoto(TelegramModel telegramModel) {
        SuccessResultModel successResultModel;
        File image = null;
        try {
            image = new File(this.getClass().getClassLoader().getResource("test.jpg").toURI());
            successResultModel = new SuccessResultModel();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            successResultModel = new SuccessResultModel("ERROR_PHOTO", "Системная ошибка при отправки картинку в телеграм");
        }
        FileOutputStream fos;
        try {
            for (PictureModel pictureModel : telegramModel.getPictures()) {
                fos = new FileOutputStream(image);
                byte[] imgByte = Base64.getDecoder().decode(pictureModel.getData());
                fos.write(imgByte);
                fos.close();
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(telegramModel.getChatId());
                sendPhoto.setPhoto(new InputFile(image));
                execute(sendPhoto);
                successResultModel = new SuccessResultModel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            successResultModel = new SuccessResultModel("ERROR_SENDING_TELEGRAM", "Не удалось отправить картинку в телеграм");
        }
        return successResultModel;
    }
}
