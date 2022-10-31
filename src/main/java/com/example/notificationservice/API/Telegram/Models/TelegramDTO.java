package com.example.notificationservice.API.Telegram.Models;

import com.example.notificationservice.API.Models.FileModel;
import com.example.notificationservice.API.Models.PictureModel;

import java.util.Date;
import java.util.List;

public class TelegramDTO {

    private String message;

    private List<FileModel> files;

    private List<PictureModel> pictures;

    private String chatId;

    private Date async;

    public TelegramDTO() {
    }

    public TelegramDTO(String message, List<FileModel> files, List<PictureModel> pictures, String chatId, Date async) {
        this.message = message;
        this.files = files;
        this.pictures = pictures;
        this.chatId = chatId;
    }

    public TelegramDTO(String message, String chatId, List<PictureModel> pictures, Date async) {
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

    public Date getAsync() {
        return async;
    }

    public void setAsync(Date async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "{\"message\":\" " + message + "\", " +
                "\"files\": [" + files + "], " +
                "\"pictures\": [" + pictures + "], " +
                "\"chatId\": \"" + chatId + "\", " +
                "\"async\": " + async + "\"}";
    }
}
