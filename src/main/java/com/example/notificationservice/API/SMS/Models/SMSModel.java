package com.example.notificationservice.API.SMS.Models;

import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;

public class SMSModel {

    private String numbers;

    private String message;

    public SMSModel() {
    }

    public SMSModel(String numbers, String message) {
        this.numbers = numbers;
        this.message = message;
    }

    public SMSModel(String data) throws ParseException {
        JSONObject obj = new JSONObject(data);
        this.numbers = obj.getString("numbers");
        this.message = obj.getString("message");
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
        return "{\"numbers\": \"" + numbers +
                "\", \"message\": \"" + message + "\"}";
    }
}
