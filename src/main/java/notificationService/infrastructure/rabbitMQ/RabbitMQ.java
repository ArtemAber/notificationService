//package notificationService.infrastructure.rabbitMQ;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.*;
//import notificationService.domain.notification.NotificationModel;
//import notificationService.domainservice.FindTaskJobService;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.UUID;
//
//@Component
//public class RabbitMQ {
//
//    private final String QUEUE_RECEIVING_TASK = "QUEUE_RECEIVING_TASK2";
//    private final String QUEUE_OUTPUT_TASK = "QUEUE_OUTPUT_TASK2";
//    private static NotificationModel notificationModel;
//    private static boolean result = false;
//
//    @Inject
//    private FindTaskJobService findTaskJobService;
//
//    @Inject
//    private Jackson2ObjectMapperBuilder mapperBuilder;
//
//    public void receivingTask(NotificationModel notificationModel) {
//        ObjectMapper objectMapper = mapperBuilder.build();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        try (Connection connection = factory.newConnection()) {
//
//            Channel channel = connection.createChannel();
//            channel.queueDeclare(QUEUE_RECEIVING_TASK, false, false, false, null);
//
//            String mes = objectMapper.writeValueAsString(notificationModel);
//            channel.basicPublish("", QUEUE_RECEIVING_TASK, null, mes.getBytes(StandardCharsets.UTF_8));
//            System.out.println("tasks send");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    public NotificationModel getTask() throws Exception {
////////        UUID id;
////////        if (notificationModel == null) {
////////            id = UUID.randomUUID();
////////        } else {
////////            id = notificationModel.getId();
////////        }
////////        getTaskFromQueue();
////////        if (id == notificationModel.getId()) {
//////            return null;
////////        } else {
////////            return notificationModel;
////////        }
////    }
//
//    @RabbitListener(queues = QUEUE_RECEIVING_TASK)
//    private void getTaskFromQueue(String message) throws Exception {
//        ObjectMapper objectMapper = mapperBuilder.build();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//
//        Channel channel = connection.createChannel();
//        channel.queueDeclare(QUEUE_RECEIVING_TASK, false, false, false, null);
//
//        notificationModel = objectMapper.readValue(message, NotificationModel.class);
//        System.out.println("i am debil" + notificationModel.getId());
//        findTaskJobService.jobTask(notificationModel);
//
////        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
////            String mes = new String(delivery.getBody(), StandardCharsets.UTF_8);
////            if (!mes.isEmpty()) {
////                convert(mes);
////                System.out.println(mes);
////            }
////            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////            System.out.println(notificationModel);
////        };
////        channel.basicConsume(QUEUE_RECEIVING_TASK, true, deliverCallback, consumerTag -> {});
//
////        Consumer consumer = new DefaultConsumer(channel);
////        channel.basicConsume(QUEUE_RECEIVING_TASK, false, consumer);
////        consumer.handleDelivery();
//
//   }
//
////   private void convert(String mes) {
////        ObjectMapper objectMapper = mapperBuilder.build();
////
////        try {
////            notificationModel = objectMapper.readValue(mes, NotificationModel.class);
////            System.out.println("convert " + notificationModel.getId());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////   }
//}
