package notificationService.domain.telegram;

import notificationService.domain.general.FileModel;
import notificationService.domain.general.PictureModel;
import java.util.List;

public class TelegramModel {

    private String message;

    private List<FileModel> files;

    private List<PictureModel> pictures;

    private String chatId;

    public TelegramModel() {
    }

    public TelegramModel(String message, List<FileModel> files, List<PictureModel> pictures, String chatId) {
        this.message = message;
        this.files = files;
        this.pictures = pictures;
        this.chatId = chatId;
    }

    public TelegramModel(String message, String chatId, List<PictureModel> pictures) {
        this.message = message;
        this.chatId = chatId;
        this.pictures = pictures;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FileModel> getFiles() {
        return files;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    public List<PictureModel> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureModel> pictures) {
        this.pictures = pictures;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
