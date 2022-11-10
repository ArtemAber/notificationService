package com.example.notificationservice.API.Email.Controllers;

import com.example.notificationservice.API.Email.Models.EmailAsyncModel;
import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Email.Service.EmailService;
import com.example.notificationservice.API.Models.GuidResultModel;
import com.example.notificationservice.API.util.EmailModelValidator;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
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

    @Inject
    private EmailService emailService;

    @Inject
    private EmailModelValidator emailModelValidator;


    @PostMapping({"/send"})
    public GuidResultModel sendAd(@RequestBody @Valid EmailModel emailModel, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailModel, bindingResult);

        if (bindingResult.hasErrors()) {
             return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
                return emailService.sendMail(emailModel);
        }
    }

    @PostMapping("/sendAsyncEmail")
    public GuidResultModel sendAdAsync(@RequestBody EmailAsyncModel emailAsyncModel, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailAsyncModel, bindingResult);

        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
            return emailService.sendAdAsync(emailAsyncModel);
        }
    }
}
