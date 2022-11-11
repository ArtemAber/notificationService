package com.example.notificationservice.API.Hibernate.DAO;

import com.example.notificationservice.API.Models.*;
import com.example.notificationservice.API.ThreadController.Models.NotificationResultModel;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public List<NotificationModel> getUnfinishedTasks(String serviceName) {
        ObjectMapper objectMapper = mapperBuilder.build();
        Session session = sessionFactory.getCurrentSession();

        session.createQuery("update NotificationEntity N set N.serviceName =:serviceName where (N.status = com.example.notificationservice.API.Models.StatusType.INIT or " +
                            "N.status = com.example.notificationservice.API.Models.StatusType.FAILED) and N.attempts < 5 " +
                            "and N.plannedDate < current_timestamp and N.serviceName = null").setParameter("serviceName", serviceName).executeUpdate();

        List<NotificationEntity> entityList = sessionFactory.getCurrentSession().createQuery("select N from NotificationEntity N WHERE " +
                                                            "(N.status = com.example.notificationservice.API.Models.StatusType.INIT or N.status = com.example.notificationservice.API.Models.StatusType.FAILED) " +
                                                            "and N.attempts < 5 " +
                                                            "and N.plannedDate < current_timestamp " +
                                                            "and N.serviceName = :serviceName", NotificationEntity.class).setParameter("serviceName", serviceName).getResultList();
        List<NotificationModel> notificationModelList = new ArrayList<>();
        for (NotificationEntity notificationEntity: entityList) {
            notificationModelList.add(objectMapper.convertValue(notificationEntity, NotificationModel.class));
        }
        return notificationModelList;
    }
    @Transactional
    public GuidResultModel saveNotification(NotificationModel addModel) {
        ObjectMapper objectMapper = mapperBuilder.build();

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
        entity.setParts(addModel.getParts());


        try {
            sessionFactory.getCurrentSession().save(entity);
            if (addModel.getMessage() == null) {
                return new GuidResultModel(id);
            } else {
                return new GuidResultModel(addModel.getMessage());
            }
        } catch (Exception e) {
            return new GuidResultModel("ERROR_SAVE_NOTIFICATION_ENTITY", "Не удалось сохранить объект");
        }
    }

    @Transactional
    public void updateNotificationEntity(UUID id, int attempts, NotificationResultModel notificationResultModel) {

        sessionFactory.getCurrentSession().createQuery("update NotificationEntity t set t.lastUpdate =:last, t.status =:status, t.message =:message, " +
                "t.attempts =:attempts, t.serviceName = null where t.id = :id")
                .setParameter("last", LocalDateTime.now())
                .setParameter("status", notificationResultModel.getStatusType())
                .setParameter("message", notificationResultModel.getErrorMessage())
                .setParameter("attempts", attempts)
                .setParameter("id", id)
                .executeUpdate();

    }

    @Transactional
    public void successfulSendingMessage(UUID id) {
        ObjectMapper objectMapper = mapperBuilder.build();
        Session session = sessionFactory.getCurrentSession();

        NotificationEntity notificationEntity = (NotificationEntity) session.createQuery("select t from NotificationEntity t where id=:id").setParameter("id", id)
                .getResultList().stream().findFirst().get();

        PartsModel partsModel;
        try {
            partsModel = objectMapper.readValue(notificationEntity.getParts(), PartsModel.class);
            partsModel.setSendMessage(true);
            String s = objectMapper.writeValueAsString(partsModel);

            session.createQuery("update NotificationEntity t set t.parts =:partsModel where t.id=:id").setParameter("partsModel", s).setParameter("id", id).executeUpdate();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void successfulSendingFiles(UUID id) {
        ObjectMapper objectMapper = mapperBuilder.build();
        Session session = sessionFactory.getCurrentSession();

        NotificationEntity notificationEntity = (NotificationEntity) session.createQuery("select t from NotificationEntity t where id=:id").setParameter("id", id)
                .getResultList().stream().findFirst().get();

        PartsModel partsModel;
        try {
            partsModel = objectMapper.readValue(notificationEntity.getParts(), PartsModel.class);
            partsModel.setSendFiles(true);
            String s = objectMapper.writeValueAsString(partsModel);

            session.createQuery("update NotificationEntity t set t.parts =:partsModel where t.id=:id").setParameter("partsModel", s).setParameter("id", id).executeUpdate();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void successfulSendingPicture(UUID id) {
        ObjectMapper objectMapper = mapperBuilder.build();
        Session session = sessionFactory.getCurrentSession();

        NotificationEntity notificationEntity = (NotificationEntity) session.createQuery("select t from NotificationEntity t where id=:id").setParameter("id", id)
                .getResultList().stream().findFirst().get();

        PartsModel partsModel;
        try {
            partsModel = objectMapper.readValue(notificationEntity.getParts(), PartsModel.class);
            partsModel.setSendPictures(true);
            String s = objectMapper.writeValueAsString(partsModel);

            session.createQuery("update NotificationEntity t set t.parts =:partsModel where t.id=:id").setParameter("partsModel", s).setParameter("id", id).executeUpdate();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateStatus(UUID id, StatusType type) {
        sessionFactory.getCurrentSession().createQuery("update NotificationEntity t set t.status =:status where t.id =:id").setParameter("status", type).setParameter("id", id).executeUpdate();
    }
}
