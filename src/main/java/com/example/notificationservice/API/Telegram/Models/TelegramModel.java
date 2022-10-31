package com.example.notificationservice.API.Telegram.Models;

import com.example.notificationservice.API.Models.FileModel;
import com.example.notificationservice.API.Models.PictureModel;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

    public TelegramModel(String datas1) throws ParseException {
        JSONObject obj = new JSONObject(datas1);

        this.message = obj.getString("message");
        this.chatId = obj.getString("chatId");

        JSONArray filesArray = obj.getJSONArray("files");
        this.files = new ArrayList<>();
        Iterator fileIter = filesArray.iterator();
        while(fileIter.hasNext()) {
           JSONObject file = (JSONObject) fileIter.next();
           this.files.add(new FileModel(file.getString("name"), file.getString("data")));
        }

        JSONArray pictureArray = obj.getJSONArray("pictures");
        this.pictures = new ArrayList<>();
        Iterator pictureIter = pictureArray.iterator();
        while (pictureIter.hasNext()) {
            JSONObject picture = (JSONObject) pictureIter.next();
            this.pictures.add(new PictureModel(picture.getString("name"), picture.getString("data")));
        }
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

    @Override
    public String toString() {
        return "{\"message\": \"" + message + "\", " +
                "\"files\": " + files + ", " +
                "\"pictures\": " + pictures + ", " +
                "\"chatId\": \"" + chatId + "\"}";
    }
}
