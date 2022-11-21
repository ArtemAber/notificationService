package notificationService.domain.email;

import notificationService.domain.general.FileModel;
import notificationService.domain.general.PictureModel;
import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.tomcat.util.json.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.Email;

public class EmailModel {

    @Email(message = "поле recipient должно быть валидным email")
    protected String email;

    protected String title;

    protected String description;

    protected List<FileModel> files;

    protected List<PictureModel> pictures;

    protected String signature;

    public EmailModel() {
    }

    public EmailModel(String email, String title, String description, List<FileModel> files, List<PictureModel> pictures, String signature) {
        this.email = email;
        this.title = title;
        this.description = description;
        this.files = files;
        this.pictures = pictures;
        this.signature = signature;
    }

    public EmailModel(String email, String title, String description, String signature) {
        this.email = email;
        this.title = title;
        this.description = description;
        this.signature = signature;
    }

    public EmailModel(String data) throws ParseException {
        JSONObject obj = new JSONObject(data);

        this.email = obj.getString("email");
        this.title = obj.getString("title");
        this.description = obj.getString("description");
        this.signature = obj.getString("signature");

        if (obj.has("files")) {
            JSONArray filesArrayObj = obj.getJSONArray("files");
            this.files = new ArrayList<>();
            Iterator fileIter = filesArrayObj.iterator();
            while (fileIter.hasNext()) {
                JSONObject file = (JSONObject) fileIter.next();
                this.files.add(new FileModel(file.getString("name"), file.getString("data")));
            }
        }

        if (obj.has("pictures")) {
            JSONArray pictureArray = obj.getJSONArray("pictures");
            this.pictures = new ArrayList<>();
            Iterator pictureIter = pictureArray.iterator();
            while (pictureIter.hasNext()) {
                JSONObject picture = (JSONObject) pictureIter.next();
                this.pictures.add(new PictureModel(picture.getString("name"), picture.getString("data")));
            }
        }
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FileModel> getFiles() {
        return this.files;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    public List<PictureModel> getPictures() {
        return this.pictures;
    }

    public void setPictures(List<PictureModel> pictures) {
        this.pictures = pictures;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
