package notificationService.domainservice;

import notificationService.domain.answers.GuidResultModel;
import notificationService.domain.answers.SuccessResultModel;
import notificationService.domain.appService.ViabilityServiceModel;
import notificationService.domain.general.FileModel;
import notificationService.domain.general.PictureModel;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationType;
import notificationService.domain.notification.StatusType;
import notificationService.domain.telegram.PartsModel;
import notificationService.infrastructure.database.NotificationDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.domain.telegram.TelegramAsyncModel;
import notificationService.domain.telegram.TelegramModel;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    private String recipient;

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    Jackson2ObjectMapperBuilder mapperBuilder;

    @Inject
    private ViabilityServiceModel viabilityServiceModel;


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
        ObjectMapper objectMapper = mapperBuilder.build();
        SuccessResultModel successResultModel = new SuccessResultModel();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_TELEGRAM);
        try {
            notificationModel.setData(objectMapper.writeValueAsString(telegramModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_TELEGRAM_MODEL", "Не удалось распарсить объект");
        }

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

        if (!successResultModel.isSuccess()) {
            return new GuidResultModel("ERROR_SENDING_TELEGRAM", "Не удалось отправить телеграм");
        } else {
            notificationModel.setStatus(StatusType.FINISHED);
        }
        return notificationDAO.saveNotification(notificationModel);
    }

    public GuidResultModel saveAsyncMes(TelegramAsyncModel telegramAsyncModel) {
        ObjectMapper objectMapper = mapperBuilder.build();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setType(NotificationType.NOTIFICATION_TELEGRAM);
        notificationModel.setPlannedDate(telegramAsyncModel.getAsync());
        try {
            notificationModel.setData(objectMapper.writeValueAsString(telegramAsyncModel));
        } catch (JsonProcessingException e) {
            return new GuidResultModel("ERROR_PARSING_ASYNC_TELEGRAM_MODEL", "Не удалось распарсить объект");
        }
        notificationModel.setStatus(StatusType.INIT);
        notificationModel.setAttempts(0);
        PartsModel partsModel = new PartsModel();

        if (telegramAsyncModel.getMessage() != null) {
            partsModel.setSendMessage(false);
        }
        if (telegramAsyncModel.getFiles() != null) {
            partsModel.setSendFiles(false);
        }
        if (telegramAsyncModel.getPictures() != null) {
            partsModel.setSendPictures(false);
        }
        try {
            notificationModel.setParts(objectMapper.writeValueAsString(partsModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notificationDAO.saveNotification(notificationModel);
    }

    public SuccessResultModel sendMessage(TelegramModel telegramModel) {
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

    public SuccessResultModel sendMsgWithFiles(TelegramModel telegramModel) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(telegramModel.getChatId());

        for (FileModel fileModel : telegramModel.getFiles()) {
            try {
                sendDocument.setDocument(new InputFile(getFile(fileModel)));
                execute(sendDocument);
            } catch (Exception e) {
                e.printStackTrace();
                return new SuccessResultModel("ERROR_SENDING_TELEGRAM", "Не удалось отправить файлы в телеграм");
            }
        }
        return new SuccessResultModel();
    }

    public File getFile(FileModel fileModel) throws Exception {

        FileOutputStream fos;
        File file = new File(fileModel.getName());
        fos = new FileOutputStream(file);
        fos.write(new String(Base64.getDecoder().decode(fileModel.getData())).getBytes());
        fos.close();
        return file;
    }

    public SuccessResultModel sendPhoto(TelegramModel telegramModel) {
        SuccessResultModel successResultModel = null;
        try {
            for (PictureModel pictureModel : telegramModel.getPictures()) {
                byte[] imgByte = Base64.getDecoder().decode(pictureModel.getData());
                InputStream inputStream = new ByteArrayInputStream(imgByte);
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(telegramModel.getChatId());
                sendPhoto.setPhoto(new InputFile(inputStream, pictureModel.getName()));
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
