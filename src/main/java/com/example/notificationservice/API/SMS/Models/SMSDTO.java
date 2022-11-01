package com.example.notificationservice.API.SMS.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class SMSDTO {

    private String numbers;

    private String message;

    private LocalDateTime async;

    public SMSDTO() {
    }

    public SMSDTO(String numbers, String message, LocalDateTime async) {
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

    public LocalDateTime getAsync() {
        return async;
    }

    public void setAsync(LocalDateTime async) {
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
