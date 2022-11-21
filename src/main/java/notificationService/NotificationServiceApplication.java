package notificationService;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import notificationService.domain.appService.ViabilityServiceModel;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
@SpringBootApplication
public class NotificationServiceApplication {

    @Value("${spring.rabbitmq.host}")
    private String host;

    public static void main(String[] args) {

        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder();
    }

    @Bean
    public ViabilityServiceModel viabilityServiceModel() {
        return new ViabilityServiceModel();
    }

    @Bean
    public Channel channel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        return channel;
    }

    @Bean
    public Queue myReceivingQueue() {
        return new Queue("QUEUE_RECEIVING_TASK" + viabilityServiceModel().getServiceName(), false, false, false, null);
    }

    @Bean
    public Queue myOutputQueue() {
        return new Queue("QUEUE_OUTPUT_TASK" + viabilityServiceModel().getServiceName(), false, false, false, null);
    }
}
