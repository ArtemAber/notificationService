package notificationService.api;

import notificationService.domain.email.EmailAsyncModel;
import notificationService.domain.email.EmailModel;
import notificationService.domainservice.EmailService;
import notificationService.domain.answers.GuidResultModel;
import notificationService.domainservice.SecurityService;
import notificationService.infrastructure.validators.EmailModelValidator;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @Inject
    private SecurityService securityService;


    @PostMapping({"/send"})
    public GuidResultModel sendAd(@RequestHeader("token") String token, @RequestBody @Valid EmailModel emailModel, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailModel, bindingResult);
        if (!securityService.checkToken(token)) {
            return new GuidResultModel("У вас нет доступа");
        }
        if (bindingResult.hasErrors()) {
             return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
                return emailService.sendMail(emailModel);
        }
    }

    @PostMapping("/sendAsyncEmail")
    public GuidResultModel sendAdAsync(@RequestHeader("token") String token, @RequestBody EmailAsyncModel emailAsyncModel, BindingResult bindingResult) {
        this.emailModelValidator.validate(emailAsyncModel, bindingResult);
        if (!securityService.checkToken(token)) {
            return new GuidResultModel("У вас нет доступа");
        }
        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
            return emailService.sendAdAsync(emailAsyncModel);
        }
    }
}
