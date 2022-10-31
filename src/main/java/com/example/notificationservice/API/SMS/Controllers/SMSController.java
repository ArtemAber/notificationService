package com.example.notificationservice.API.SMS.Controllers;

import com.example.notificationservice.API.Hibernate.service.TaskHibernateService;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.SMS.Models.SMSDTO;
import com.example.notificationservice.API.SMS.Models.SMSModel;
import com.example.notificationservice.API.SMS.Service.SMSService;
import com.example.notificationservice.API.ThreadController.Models.TaskModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/sms")
@Tag(name = "контроллер для отправки смс", description = "отправляет смс на номер телефона")
public class SMSController {

    private final SMSService smsService;
    private final ModelMapper modelMapper;
    private final TaskHibernateService taskHibernateService;

    @Autowired
    public SMSController(SMSService smsService, ModelMapper modelMapper, TaskHibernateService taskHibernateService) {
        this.smsService = smsService;
        this.modelMapper = modelMapper;
        this.taskHibernateService = taskHibernateService;
    }

    @PostMapping("/send")
    public String sendSMS(@RequestBody @Valid SMSDTO smsdto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage();
        } else {
            return smsService.saveSMS(convertToSMSModel(smsdto), NotificationType.NOTIFICATION_SMS);
        }
    }

    @PostMapping("/sendAsyncSMS")
    public void sendSMSAsync(@RequestBody @Valid SMSDTO smsdto, BindingResult bindingResult) {
        String answer = "Сообщение принято";

        if (bindingResult.hasErrors()) {
            answer = bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage();
        } else {
            Date date = smsdto.getAsync();
            date.setHours(date.getHours() - 3);
            taskHibernateService.saveTask(new TaskModel(NotificationType.NOTIFICATION_SMS, convertToSMSModel(smsdto).toString(), date));
        }
    }

    private SMSModel convertToSMSModel(SMSDTO smsdto) {
        return modelMapper.map(smsdto, SMSModel.class);
    }
}
