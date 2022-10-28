package com.example.notificationservice.API.SMS.Models;

import java.util.Arrays;

public class SMSModel {

    private String numbers;

    private String message;

    public SMSModel() {
    }

    public SMSModel(String numbers, String message) {
        this.numbers = numbers;
        this.message = message;
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

    @Override
    public String toString() {
        return "SmsModel{" +
                "numbers=" + numbers +
                ", message='" + message + '\'' +
                '}';
    }
}
