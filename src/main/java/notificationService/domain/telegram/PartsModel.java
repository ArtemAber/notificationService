package notificationService.domain.telegram;

public class PartsModel {

    private Boolean sendMessage;

    private Boolean sendFiles;

    private Boolean sendPictures;

    public Boolean isSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Boolean isSendFiles() {
        return sendFiles;
    }

    public void setSendFiles(Boolean sendFiles) {
        this.sendFiles = sendFiles;
    }

    public Boolean isSendPictures() {
        return sendPictures;
    }

    public void setSendPictures(Boolean sendPictures) {
        this.sendPictures = sendPictures;
    }

    public Boolean shippedMessage() {
        if (Boolean.TRUE.equals(sendMessage) || sendMessage == null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean shippedFiles() {
        if (Boolean.TRUE.equals(sendFiles) || sendFiles == null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean shippedPictures() {
        if (Boolean.TRUE.equals(sendPictures) || sendPictures == null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
