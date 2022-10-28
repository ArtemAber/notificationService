package com.example.notificationservice.API.util;

import com.example.notificationservice.API.Email.Models.EmailModel;
import com.example.notificationservice.API.Models.FileModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailModelValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EmailModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailModel emailModel = (EmailModel) target;

        if (emailModel.getFiles() != null) {
            for (FileModel fileModel: emailModel.getFiles()) {
                if (!(fileModel.getName().endsWith(".txt") || fileModel.getName().endsWith(".docx") || fileModel.getName().endsWith(".jpg") || fileModel.getName().endsWith(".pptx"))) {
                    errors.rejectValue("files", "", "Укажите корректное наименование файла " + fileModel.getName());
                }
            }
        }
    }
}
