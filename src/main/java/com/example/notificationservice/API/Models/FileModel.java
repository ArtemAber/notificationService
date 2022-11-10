package com.example.notificationservice.API.Models;

import javax.validation.constraints.NotEmpty;

public class FileModel {

    @NotEmpty
    private String name;

    @NotEmpty
    private String data;

    public FileModel(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
