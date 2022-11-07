package com.example.notificationservice.API.Telegram.Models;

import java.time.LocalDateTime;

public class TelegramAsyncModel extends TelegramModel {

    private LocalDateTime async;

    public LocalDateTime getAsync() {
        return async;
    }

    public void setAsync(LocalDateTime async) {
        this.async = async;
    }
}
