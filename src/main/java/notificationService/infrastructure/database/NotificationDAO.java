package notificationService.infrastructure.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.domain.answers.GuidResultModel;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.NotificationResultModel;
import notificationService.domain.notification.StatusType;
import notificationService.domain.telegram.PartsModel;
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
    public List<NotificationModel> getUnfinishedTasks(String serviceName, int maxResult) {

        Session session = sessionFactory.getCurrentSession();

            session.createSQLQuery("update notification set service_name=:serviceName where id in " +
                    "(select n.id from notification n where (n.status='INIT' or n.status='FAILED') " +
                    "and n.attempts < 5 " +
                    "and n.planned_date < current_timestamp " +
                    "and n.service_name is null order by n.planned_date asc limit :maxRes)")
                    .setParameter("serviceName", serviceName)
                    .setParameter("maxRes", maxResult)
                    .addEntity(NotificationEntity.class)
                    .executeUpdate();

        ObjectMapper objectMapper = mapperBuilder.build();

        List<NotificationEntity> entityList = session.createQuery("select N from NotificationEntity N WHERE " +
                "(N.status =:statusInit or N.status =:statusFailed ) " +
                "and N.attempts < 5 " +
                "and N.plannedDate < current_timestamp " +
                "and N.serviceName =:serviceName", NotificationEntity.class)
                .setParameter("statusInit", StatusType.INIT)
                .setParameter("statusFailed", StatusType.FAILED)
                .setParameter("serviceName", serviceName).setMaxResults(maxResult).getResultList();

        session.createQuery("update NotificationEntity set status=:status WHERE " +
                "(status =:statusInit or status=:statusFailed) " +
                "and attempts < 5 " +
                "and plannedDate < current_timestamp " +
                "and serviceName=:serviceName")
                .setParameter("status", StatusType.IN_THE_QUEUE)
                .setParameter("statusInit", StatusType.INIT)
                .setParameter("statusFailed", StatusType.FAILED)
                .setParameter("serviceName", serviceName).executeUpdate();

        List<NotificationModel> notificationModelList = new ArrayList<>();
        for (NotificationEntity notificationEntity : entityList) {
            notificationModelList.add(objectMapper.convertValue(notificationEntity, NotificationModel.class));
        }
        return notificationModelList;
    }

    @Transactional
    public void toFreeTask(String serviceName) {
        sessionFactory.getCurrentSession().createQuery("update NotificationEntity set status=:status, " +
                "serviceName=null where serviceName=:serviceName")
                .setParameter("status", StatusType.FAILED)
                .setParameter("serviceName", serviceName).executeUpdate();
    }

    @Transactional
    public GuidResultModel saveNotification(NotificationModel addModel) {

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

            session.createQuery("update NotificationEntity t set t.parts =:partsModel where t.id=:id").setParameter("partsModel", s).setParameter("id", id)
                    .executeUpdate();
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

            session.createQuery("update NotificationEntity t set t.parts =:partsModel where t.id=:id").setParameter("partsModel", s).setParameter("id", id)
                    .executeUpdate();
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

            session.createQuery("update NotificationEntity t set t.parts =:partsModel where t.id=:id").setParameter("partsModel", s).setParameter("id", id)
                    .executeUpdate();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateStatus(UUID id, StatusType type) {
        sessionFactory.getCurrentSession().createQuery("update NotificationEntity t set t.status =:status where t.id =:id").setParameter("status", type)
                .setParameter("id", id).executeUpdate();
    }
}
