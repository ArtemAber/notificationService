package com.example.notificationservice.API.Hibernate.DAO;

import com.example.notificationservice.API.Models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
public class NotificationDAO {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Transactional
    public List<NotificationModel> getUnfinishedTasks() {
        ObjectMapper objectMapper = mapperBuilder.build();
        List<NotificationEntity> entityList = sessionFactory.getCurrentSession().createQuery("select N from NotificationEntity N WHERE " +
                                                            "N.status <> com.example.notificationservice.API.Models.StatusType.FINISHED " +
                                                            "and N.attempts < 5 " +
                                                            "and N.plannedDate < current_timestamp", NotificationEntity.class).getResultList();
        List<NotificationModel> notificationModelList = new ArrayList<>();
        for (NotificationEntity notificationEntity: entityList) {
            notificationModelList.add(objectMapper.convertValue(notificationEntity, NotificationModel.class));
        }
        return notificationModelList;
    }
    @Transactional
    public GuidResultModel saveNotification(NotificationModel addModel, SuccessResultModel successResultModel) {

        NotificationEntity entity = new NotificationEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setType(addModel.getType());
        entity.setData(addModel.getData());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastUpdate(LocalDateTime.now());
        entity.setStatus(addModel.getStatus());
        entity.setMessage(addModel.getMessage());
        entity.setAttempts(addModel.getAttempts());
        entity.setPlannedDate(addModel.getPlannedDate());

        try {
            sessionFactory.getCurrentSession().save(entity);
            return new GuidResultModel(id, successResultModel.getErrorCode(), successResultModel.getErrorMessage());
        } catch (Exception e) {
            return new GuidResultModel(id, "ERROR_SAVE_NOTIFICATION_ENTITY", "Не удалось сохранить объект");
        }
    }

    @Transactional
    public void updateNotificationEntity(NotificationModel updateModel) {

        ObjectMapper objectMapper = mapperBuilder.build();
        NotificationEntity notificationEntity = objectMapper.convertValue(updateModel, NotificationEntity.class);

        notificationEntity.setLastUpdate(LocalDateTime.now());
        notificationEntity.setAttempts(updateModel.getAttempts() + 1);


        sessionFactory.getCurrentSession().update(notificationEntity);
    }
}
