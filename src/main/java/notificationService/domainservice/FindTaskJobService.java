package notificationService.domainservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import notificationService.domain.answers.OutOfQueueToDBModel;
import notificationService.infrastructure.database.NotificationDAO;
import notificationService.infrastructure.database.ViabilityServiceDAO;
import notificationService.domain.notification.NotificationModel;
import notificationService.domain.notification.StatusType;
import notificationService.domain.appService.ViabilityServiceModel;
import notificationService.domain.notification.NotificationResultModel;
import notificationService.domainservice.strategys.TaskNotificationExecutor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class FindTaskJobService {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private ViabilityServiceDAO viabilityServiceDAO;

    @Inject
    private TaskNotificationExecutor taskNotificationExecutor;

    @Inject
    private ViabilityServiceModel viabilityServiceModel;

    @Inject
    private Channel channel;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Inject
    private Queue myReceivingQueue;

    @Inject
    private Queue myOutputQueue;


//    @Value("QUEUE_RECEIVING_TASK" + "${viabilityServiceModel.getServiceName()}")
//    private final String QUEUE_RECEIVING_TASK = "QUEUE_RECEIVING_TASK" + viabilityServiceModel.getServiceName();
//
//    @Value("QUEUE_OUTPUT_TASK" + "${viabilityServiceModel.getServiceName()}")
//    private String QUEUE_OUTPUT_TASK;

    @Scheduled(initialDelay = 1000, fixedRateString = "3000")
    public void toFreeTask() {
        List<ViabilityServiceModel> serviceModelList = viabilityServiceDAO.getAllViabilityService();
        for(ViabilityServiceModel serviceModel: serviceModelList) {
            notificationDAO.toFreeTask(serviceModel.getServiceName());
            viabilityServiceDAO.deleteViabilityService(serviceModel.getId());
        }
    }

    @Scheduled(initialDelay = 10000, fixedRateString = "1000")
    public void findTask() {
            List<NotificationModel> notificationModelList = notificationDAO.getUnfinishedTasks(viabilityServiceModel.getServiceName(), 5);
            for (NotificationModel notificationModel: notificationModelList) {
                receivingTask(notificationModel);
            }
    }

    public void jobTask(NotificationModel notificationModel) {
            if (notificationModel != null) {
                CompletableFuture.supplyAsync(() -> {
                    notificationDAO.updateStatus(notificationModel.getId(), StatusType.PROCESSING);
                    return taskNotificationExecutor.runTask(notificationModel);
                }).thenAccept(notificationResultModel -> {
                    receivedAnswer(notificationModel.getId(), notificationModel.getAttempts() + 1, notificationResultModel);
                });
            }
    }

    private boolean updateTask(OutOfQueueToDBModel outOfQueueToDBModel) {
        notificationDAO.updateNotificationEntity(outOfQueueToDBModel.getId(), outOfQueueToDBModel.getAttempts(), outOfQueueToDBModel.getNotificationResultModel());
        return true;
    }

    @Scheduled(initialDelay = 10000, fixedRateString = "30000")
    public void updateDataService() {
        viabilityServiceModel.setLastUpdate(LocalDateTime.now());
        viabilityServiceDAO.saveService(viabilityServiceModel);
    }

    public void receivingTask(NotificationModel notificationModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(host);
//        try (Connection connection = factory.newConnection()) {
//
//            Channel channel = connection.createChannel();
        try {
            channel.queueDeclare("QUEUE_RECEIVING_TASK" + viabilityServiceModel.getServiceName(), false, false, false, null);

            String mes = objectMapper.writeValueAsString(notificationModel);
            channel.basicPublish("", "QUEUE_RECEIVING_TASK" + viabilityServiceModel.getServiceName(), null, mes.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "#{myReceivingQueue}")
    private void getTaskFromQueue(String message) throws Exception {
        ObjectMapper objectMapper = mapperBuilder.build();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(host);
//        Connection connection = factory.newConnection();
//
//        Channel channel = connection.createChannel();
        channel.queueDeclare("QUEUE_RECEIVING_TASK" + viabilityServiceModel.getServiceName(), false, false, false, null);

        NotificationModel notificationModel = objectMapper.readValue(message, NotificationModel.class);
        jobTask(notificationModel);
    }

    public void receivedAnswer(UUID id, int attempts, NotificationResultModel notificationResultModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(host);
//        try (Connection connection = factory.newConnection()) {
//
//            Channel channel = connection.createChannel();
        try {
            channel.queueDeclare("QUEUE_OUTPUT_TASK" + viabilityServiceModel.getServiceName(), false, false, false, null);

            OutOfQueueToDBModel outOfQueueToDBModel = new OutOfQueueToDBModel(id, attempts, notificationResultModel);

            channel.basicPublish("", "QUEUE_OUTPUT_TASK" + viabilityServiceModel.getServiceName(), null, objectMapper.writeValueAsString(outOfQueueToDBModel).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "#{myOutputQueue}")
    private void getAnswerFromQueue(String message) throws Exception {
        ObjectMapper objectMapper = mapperBuilder.build();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(host);
//        Connection connection = factory.newConnection();
//
//        Channel channel = connection.createChannel();
        channel.queueDeclare("QUEUE_OUTPUT_TASK" + viabilityServiceModel.getServiceName(), false, false, false, null);

        OutOfQueueToDBModel outOfQueueToDBModel = objectMapper.readValue(message, OutOfQueueToDBModel.class);

        updateTask(outOfQueueToDBModel);
    }
}

