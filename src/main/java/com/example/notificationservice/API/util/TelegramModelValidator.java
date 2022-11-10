package com.example.notificationservice.API.util;

import com.example.notificationservice.API.Models.FileModel;
import com.example.notificationservice.API.Models.PictureModel;
import com.example.notificationservice.API.Telegram.Models.TelegramModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;

@Component
public class TelegramModelValidator implements Validator {

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Override
    public boolean supports(Class<?> clazz) {
        return TelegramModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TelegramModel telegramModel = (TelegramModel) target;
        ObjectMapper objectMapper = mapperBuilder.build();

        try {
            String check = objectMapper.writeValueAsString(telegramModel);
        } catch (Exception e) {
            errors.rejectValue("","", "Не удалось распарсить данные");
        }

        if (telegramModel.getFiles() != null) {
            for (FileModel file : telegramModel.getFiles()) {
                if (!(file.getName().endsWith(".txt") || file.getName().endsWith(".docx") || file.getName().endsWith(".jpg") || file.getName().endsWith(".pptx"))) {
                    errors.rejectValue("files", "", "Укажите корректное наименование файла " + file.getName());
                } else {
                    try {
                        String check = objectMapper.writeValueAsString(file);

                        if (file.getData().trim().length() == 0) {
                            errors.rejectValue("files", "", "Файл: " + file.getName() + " не должен быть пустым");
                        }

                    } catch (Exception e) {
                        errors.rejectValue("files", "", "Не удалось распарсить объект: " + file.getName());
                    }
                }
            }
        }

        if (telegramModel.getPictures() != null) {
            for (PictureModel picture : telegramModel.getPictures()) {
                if (!(picture.getName().endsWith(".jpg") || picture.getName().endsWith(".jpeg") || picture.getName().endsWith(".png") || picture.getName().endsWith(".gif"))) {
                    errors.rejectValue("pictures", "", "Укажите корректное наименование файла " + picture.getName());
                } else {
                    try {
                        String check = objectMapper.writeValueAsString(picture);

                        if (picture.getData().trim().length() == 0) {
                            errors.rejectValue("pictures", "", "Картинка: " + picture.getName() + " не должна быть пустой");
                        }

                    } catch (Exception e) {
                        errors.rejectValue("pictures", "", "Не удалось распарсить объект: " + picture.getName());
                    }
                }
            }
        }
    }
}
