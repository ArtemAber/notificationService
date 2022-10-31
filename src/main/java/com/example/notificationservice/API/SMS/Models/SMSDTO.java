package com.example.notificationservice.API.SMS.Models;

import java.util.Date;

public class SMSDTO {

    private String numbers;

    private String message;

    private Date async;

    public SMSDTO() {
    }

    public SMSDTO(String numbers, String message, Date async) {
        this.numbers = numbers;
        this.message = message;
        this.async = async;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getAsync() {
        return async;
    }

    public void setAsync(Date async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "SMSDTO{" +
                "numbers='" + numbers + '\'' +
                ", message='" + message + '\'' +
                ", async=" + async +
                '}';
    }
}
