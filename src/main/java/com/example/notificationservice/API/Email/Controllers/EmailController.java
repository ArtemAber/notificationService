package com.example.notificationservice.API.Email.Controllers;

import com.example.notificationservice.API.Models.NotificationType;
import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Email.Service.EmailService;
import com.example.notificationservice.API.util.EmailModelValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/regularAd"})
@Tag(
        name = "Контроллер писем по Email",
        description = "Отправляет письма на почту"
)
public class EmailController {
    private final EmailService emailService;
    private final EmailModelValidator emailModelValidator;

    @Autowired
    public EmailController(EmailService emailService, EmailModelValidator emailModelValidator) {
        this.emailService = emailService;
        this.emailModelValidator = emailModelValidator;
    }

    @PostMapping({"/send"})
    public String sendAd(@RequestBody @Valid EmailModel emailModel, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailModel, bindingResult);
        return bindingResult.hasErrors() ? ((ObjectError)bindingResult.getAllErrors().stream().findAny().get()).getDefaultMessage() : this.emailService.saveSimpleMail(emailModel, NotificationType.NOTIFICATION_EMAIL);
    }
}
