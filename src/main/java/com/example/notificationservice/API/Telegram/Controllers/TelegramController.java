package com.example.notificationservice.API.Telegram.Controllers;

import com.example.notificationservice.API.Models.GuidResultModel;
import com.example.notificationservice.API.Telegram.Models.TelegramAsyncModel;
import com.example.notificationservice.API.Telegram.Models.TelegramBot;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
import com.example.notificationservice.API.util.TelegramModelValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/telegram")
@Tag(name = "Контроллер для телеграма", description = "отправляет данные в телеграм")
public class TelegramController {

    @Inject
    private TelegramBot telegramBot;

    @Inject
    private TelegramModelValidator telegramModelValidator;


    @PostMapping("/sendMessage")
    public GuidResultModel sendMessage(@RequestBody TelegramModel telegramModel, BindingResult bindingResult) {
        telegramModelValidator.validate(telegramModel, bindingResult);

        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        return telegramBot.sendMes(telegramModel);
    }

    @PostMapping("/sendAsyncMessage")
    public GuidResultModel sendAsyncMessage(@RequestBody TelegramAsyncModel telegramAsyncModel, BindingResult bindingResult) {
        telegramModelValidator.validate(telegramAsyncModel, bindingResult);

        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        return telegramBot.saveAsyncMes(telegramAsyncModel);
    }
}
