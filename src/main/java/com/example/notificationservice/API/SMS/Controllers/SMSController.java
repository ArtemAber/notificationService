package com.example.notificationservice.API.SMS.Controllers;

import com.example.notificationservice.API.Models.GuidResultModel;
import com.example.notificationservice.API.SMS.Models.SMSAsyncModel;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.example.notificationservice.API.SMS.Service.SMSService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/sms")
@Tag(name = "контроллер для отправки смс", description = "отправляет смс на номер телефона")
public class SMSController {

    @Inject
    private SMSService smsService;

    @PostMapping("/send")
    public GuidResultModel sendSMS(@RequestBody @Valid SMSModel smsModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
            return smsService.saveSMS(smsModel);
        }
    }

    @PostMapping("/sendAsyncSMS")
    public GuidResultModel sendSMSAsync(@RequestBody @Valid SMSAsyncModel smsAsyncModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
            return smsService.saveAsyncSMS(smsAsyncModel);
        }
    }
}
