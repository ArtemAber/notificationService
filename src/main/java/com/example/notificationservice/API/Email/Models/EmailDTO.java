package com.example.notificationservice.API.Email.Models;

import com.example.notificationservice.API.Models.FileModel;
import com.example.notificationservice.API.Models.PictureModel;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;

public class EmailDTO {

    @Email(message = "поле recipient должно быть валидным email")
    private String email;

    private String title;

    private String description;

    private List<FileModel> files;

    private List<PictureModel> pictures;

    private String signature;

    private Date async;

    public EmailDTO() {
    }

    public EmailDTO(String email, String title, String description, List<FileModel> files, List<PictureModel> pictures, String signature, Date async) {
        this.email = email;
        this.title = title;
        this.description = description;
        this.files = files;
        this.pictures = pictures;
        this.signature = signature;
        this.async = async;
    }

    public EmailDTO(String email, String title, String description, String signature, Date async) {
        this.email = email;
        this.title = title;
        this.description = description;
        this.signature = signature;
        this.async = async;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FileModel> getFiles() {
        return this.files;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    public List<PictureModel> getPictures() {
        return this.pictures;
    }

    public void setPictures(List<PictureModel> pictures) {
        this.pictures = pictures;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getAsync() {
        return async;
    }

    public void setAsync(Date async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", files=" + files +
                ", pictures=" + pictures +
                ", signature='" + signature + '\'' +
                ", async=" + async +
                '}';
    }
}
