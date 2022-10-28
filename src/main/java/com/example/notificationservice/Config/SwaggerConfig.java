package com.example.notificationservice.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Notification service")
                                            .version("1.0.0")
                                            .contact(new Contact().name("Sokolov Artem")));
    }
}
