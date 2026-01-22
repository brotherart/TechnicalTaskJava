package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Property View API")
                        .version("1.0")
                        .description("API для управления отелями согласно техническому заданию"))
                .addServersItem(new Server()
                        .url("/property-view")
                        .description("Основной сервер"));
    }

    @Bean
    public GroupedOpenApi hotelApi() {
        return GroupedOpenApi.builder()
                .group("hotels")
                .pathsToMatch("/hotels/**")
                .build();
    }
}