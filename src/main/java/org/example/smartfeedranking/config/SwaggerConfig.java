package org.example.smartfeedranking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Feed_Ranking",
                version = "1.0.0",
                contact = @Contact(name = "Bexruz")
        ),
        security = @SecurityRequirement(name = "Bearer Token")
)

public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer serverUrlCustomizer() {
        return openApi -> {
            Server server = new Server();
            server.setUrl("/");
            server.setDescription("Auto detected server");
            openApi.setServers(List.of(server));
        };
    }
}