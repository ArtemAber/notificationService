package notificationService.domain.sms;

import java.time.LocalDateTime;

public class SMSAsyncModel extends SMSModel {

    private LocalDateTime async;

    public LocalDateTime getAsync() {
        return async;
    }

    public void setAsync(LocalDateTime async) {
        this.async = async;
    }
}
