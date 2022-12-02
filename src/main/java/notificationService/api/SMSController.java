package notificationService.api;

import notificationService.domain.answers.GuidResultModel;
import notificationService.domain.sms.SMSAsyncModel;
import notificationService.domain.sms.SMSModel;
import notificationService.domainservice.SMSService;
import io.swagger.v3.oas.annotations.tags.Tag;
import notificationService.domainservice.SecurityService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/sms")
@Tag(name = "контроллер для отправки смс", description = "отправляет смс на номер телефона")
public class SMSController {

    @Inject
    private SMSService smsService;

    @Inject
    private SecurityService securityService;

    @PostMapping("/send")
    public GuidResultModel sendSMS(@RequestHeader("token") String token, @RequestBody @Valid SMSModel smsModel, BindingResult bindingResult) {
        if (!securityService.checkToken(token)) {
            return new GuidResultModel("У вас нет доступа");
        }
        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
            return smsService.saveSMS(smsModel);
        }
    }

    @PostMapping("/sendAsyncSMS")
    public GuidResultModel sendSMSAsync(@RequestHeader("token") String token, @RequestBody @Valid SMSAsyncModel smsAsyncModel, BindingResult bindingResult) {
        if (!securityService.checkToken(token)) {
            return new GuidResultModel("У вас нет доступа");
        }
        if (bindingResult.hasErrors()) {
            return new GuidResultModel("DATA_ERRORS", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        } else {
            return smsService.saveAsyncSMS(smsAsyncModel);
        }
    }
}
