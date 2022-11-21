package notificationService.domain.notification;

public class NotificationResultModel {

    private StatusType statusType;

    private String errorCode;

    private String errorMessage;

    public NotificationResultModel() {
        this.statusType = StatusType.FINISHED;
    }

    public NotificationResultModel(String errorCode, String errorMessage) {
        this.statusType = StatusType.FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
