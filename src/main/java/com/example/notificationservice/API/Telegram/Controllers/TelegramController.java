package com.example.notificationservice.API.Telegram.Controllers;

import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
import com.example.notificationservice.API.Telegram.Service.TelegramService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
@Tag(name = "Контроллер для телеграма", description = "отправляет данные в телеграм")
public class TelegramController {

    private final TelegramService telegramService;

    public TelegramController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody TelegramModel telegramModel) {
        telegramService.saveMessage(telegramModel, NotificationType.NOTIFICATION_TELEGRAM);
    }
}
