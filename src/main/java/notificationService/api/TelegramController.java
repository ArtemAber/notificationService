package notificationService.api;

import notificationService.domain.answers.GuidResultModel;
import notificationService.domain.telegram.TelegramAsyncModel;
import notificationService.domainservice.TelegramBot;
import notificationService.domain.telegram.TelegramModel;
import notificationService.domainservice.SecurityService;
import notificationService.infrastructure.validators.TelegramModelValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.security.Principal;

@RestController
@RequestMapping("/telegram")
@Tag(name = "Контроллер для телеграма", description = "отправляет данные в телеграм")
public class TelegramController {

    @Inject
    private TelegramBot telegramBot;

    @Inject
    private TelegramModelValidator telegramModelValidator;

    @Inject
    private SecurityService securityService;


    @PostMapping("/sendMessage")
    public GuidResultModel sendMessage(@RequestHeader("token") String token, @RequestBody TelegramModel telegramModel, Principal principal, BindingResult bindingResult) {
        telegramModelValidator.validate(telegramModel, bindingResult);
        if (!securityService.checkToken(token)) {
            return new GuidResultModel("У вас нет доступа");
        }
        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        return telegramBot.sendMes(telegramModel);
    }

    @PostMapping("/sendAsyncMessage")
    public GuidResultModel sendAsyncMessage(@RequestHeader("token") String token, @RequestBody TelegramAsyncModel telegramAsyncModel, BindingResult bindingResult) {
        telegramModelValidator.validate(telegramAsyncModel, bindingResult);

        if (!securityService.checkToken(token)) {
            return new GuidResultModel("У вас нет доступа");
        }
        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        return telegramBot.saveAsyncMes(telegramAsyncModel);
    }
}
