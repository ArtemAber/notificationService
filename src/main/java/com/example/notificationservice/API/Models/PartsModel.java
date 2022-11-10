package com.example.notificationservice.API.Models;

public class PartsModel {

    private Boolean sendMessage;

    private Boolean sendFiles;

    private Boolean sendPictures;

    public Boolean isSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Boolean isSendFiles() {
        return sendFiles;
    }

    public void setSendFiles(Boolean sendFiles) {
        this.sendFiles = sendFiles;
    }

    public Boolean isSendPictures() {
        return sendPictures;
    }

    public void setSendPictures(Boolean sendPictures) {
        this.sendPictures = sendPictures;
    }

    @Override
    public String toString() {
        return "PartsModel{" +
                "sendMessage=" + sendMessage +
                ", sendFiles=" + sendFiles +
                ", sendPictures=" + sendPictures +
                '}';
    }
}
