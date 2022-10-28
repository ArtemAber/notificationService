package com.example.notificationservice.API.SMS.Controllers;

import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.example.notificationservice.API.SMS.Service.SMSService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sms")
@Tag(name = "контроллер для отправки смс", description = "отправляет смс на номер телефона")
public class SMSController {

    private final SMSService SMSService;

    @Autowired
    public SMSController(SMSService SMSService) {
        this.SMSService = SMSService;
    }

    @PostMapping("/send")
    public String sendSMS(@RequestBody @Valid SMSModel smsModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage();
        }

        return SMSService.saveSMS(smsModel, NotificationType.NOTIFICATION_SMS);
    }
}
