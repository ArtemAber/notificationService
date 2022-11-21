package notificationService.domain.email;

import java.time.LocalDateTime;

public class EmailAsyncModel extends EmailModel {

    private LocalDateTime async;

    public LocalDateTime getAsync() {
        return async;
    }

    public void setAsync(LocalDateTime async) {
        this.async = async;
    }
}
