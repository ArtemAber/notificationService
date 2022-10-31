package com.example.notificationservice.API.Telegram.Controllers;

import com.example.notificationservice.API.Hibernate.service.TaskHibernateService;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Telegram.Models.TelegramDTO;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
import com.example.notificationservice.API.Telegram.Service.TelegramService;
import com.example.notificationservice.API.ThreadController.Models.TaskModel;
import com.example.notificationservice.API.ThreadController.ThreadCreator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/telegram")
@Tag(name = "Контроллер для телеграма", description = "отправляет данные в телеграм")
public class TelegramController {

    private final TelegramService telegramService;
    private final ModelMapper modelMapper;
    private final ThreadCreator threadCreator;
    private final TaskHibernateService taskHibernateService;

    public TelegramController(TelegramService telegramService, ModelMapper modelMapper, ThreadCreator threadCreator, TaskHibernateService taskHibernateService) {
        this.telegramService = telegramService;
        this.modelMapper = modelMapper;
        this.threadCreator = threadCreator;
        this.taskHibernateService = taskHibernateService;
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody TelegramModel telegramModel) {
        telegramService.saveMessage(telegramModel, NotificationType.NOTIFICATION_TELEGRAM);
    }

    @PostMapping("/sendAsyncMessage")
    public void sendAsyncMessage(@RequestBody TelegramDTO telegramDTO) {
        Date date = telegramDTO.getAsync();
        date.setHours(date.getHours() - 3);
        taskHibernateService.saveTask(new TaskModel(NotificationType.NOTIFICATION_TELEGRAM, convertToTelegramModel(telegramDTO).toString(), date));
    }

    private TelegramModel convertToTelegramModel(TelegramDTO telegramDTO) {
        return modelMapper.map(telegramDTO, TelegramModel.class);
    }
}
