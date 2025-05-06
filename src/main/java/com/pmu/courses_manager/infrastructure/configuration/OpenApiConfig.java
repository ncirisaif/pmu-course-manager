package com.pmu.courses_manager.infrastructure.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration de SpringDoc OpenAPI pour la documentation de l'API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion des Courses")
                        .description("API pour la gestion des courses sportives et des participants")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe de développement")
                                .email("contact@example.com")
                                .url("https://www.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("/").description("Serveur par défaut")
                ))
                .components(new Components()
                        // Définition des schémas pour les réponses d'erreur
                        .addSchemas("ErrorResponse", new io.swagger.v3.oas.models.media.Schema<>()
                                .type("object")
                                .addProperties("code", new io.swagger.v3.oas.models.media.StringSchema())
                                .addProperties("message", new io.swagger.v3.oas.models.media.StringSchema())
                                .description("Représentation standardisée d'une erreur API")
                        )
                );
    }
}