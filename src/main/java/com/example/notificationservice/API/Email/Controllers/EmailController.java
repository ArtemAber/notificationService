package com.example.notificationservice.API.Email.Controllers;

import com.example.notificationservice.API.Email.Models.EmailDTO;
import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Email.Service.EmailService;
import com.example.notificationservice.API.Hibernate.service.TaskHibernateService;
import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.ThreadController.Models.TaskModel;
import com.example.notificationservice.API.util.EmailModelValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping({"/regularAd"})
@Tag(
        name = "Контроллер писем по Email",
        description = "Отправляет письма на почту"
)
public class EmailController {
    private final EmailService emailService;
    private final EmailModelValidator emailModelValidator;
    private final ModelMapper modelMapper;
    private final TaskHibernateService taskHibernateService;

    @Autowired
    public EmailController(EmailService emailService, EmailModelValidator emailModelValidator, ModelMapper modelMapper, TaskHibernateService taskHibernateService) {
        this.emailService = emailService;
        this.emailModelValidator = emailModelValidator;
        this.modelMapper = modelMapper;
        this.taskHibernateService = taskHibernateService;
    }

    @PostMapping({"/send"})
    public String sendAd(@RequestBody @Valid EmailDTO emailDTO, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailDTO, bindingResult);

        String answer = "ответ не пришёл";

        if (bindingResult.hasErrors()) {
            answer = (bindingResult.getAllErrors().stream().findAny().get()).getDefaultMessage();
        } else {
                answer = emailService.saveMail(convertToEmailModel(emailDTO), NotificationType.NOTIFICATION_EMAIL);
        }
        return answer;
    }

    @PostMapping("/sendAsyncEmail")
    public String sendAdAsync(@RequestBody @Valid EmailDTO emailDTO, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailDTO, bindingResult);

        String answer = "Сообщение принято";

        if (bindingResult.hasErrors()) {
            answer = (bindingResult.getAllErrors().stream().findAny().get()).getDefaultMessage();
        } else {
            Date date = emailDTO.getAsync();
            date.setHours(date.getHours() - 3);
            taskHibernateService.saveTask(new TaskModel(NotificationType.NOTIFICATION_EMAIL, convertToEmailModel(emailDTO).toString(), date));
        }
        return answer;
    }

    private EmailModel convertToEmailModel(EmailDTO emailDTO) {
        return modelMapper.map(emailDTO, EmailModel.class);
    }
}
