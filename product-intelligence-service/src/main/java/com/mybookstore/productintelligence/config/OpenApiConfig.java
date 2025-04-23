package com.mybookstore.productintelligence.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Value("${WEB_DOMAIN:localhost}")
        private String webDomain;

        @Value("${server.port:8085}")
        private String serverPort;

        @Value("${SPRING_CONTACT_NAME:DNguyen}")
        private String contactName;

        @Value("${SPRING_CONTACT_URL:https://github.com/thanhtuanh}")
        private String contactUrl;

        @Value("${SPRING_CONTACT_EMAIL:n.thanh@gmx.de}")
        private String contactEmail;

        @Bean
        public OpenAPI customOpenAPI() {
                String protocol = webDomain.contains("localhost") ? "http" : "https";
                // Port nur für lokale Entwicklung hinzufügen, nicht für Produktionsumgebung
                String serverUrl = protocol + "://" + webDomain;

                // Port nur für lokale Entwicklung hinzufügen
                if (webDomain.contains("localhost") || webDomain.equals("127.0.0.1")) {
                        serverUrl += ":" + serverPort;
                }

                return new OpenAPI()
                                .servers(List.of(
                                                new Server()
                                                                .url(serverUrl)
                                                                .description(protocol.equals("https")
                                                                                ? "Produktivsystem"
                                                                                : "Lokale Entwicklung")))
                                .info(new Info()
                                                .title("PIM AI Connector – Product Intelligence API")
                                                .version("1.0.0")
                                                .description("Diese API erzeugt KI-gestützte Buchbeschreibungen, analysiert Buchcover und verarbeitet Produktinformationen automatisch.")
                                                .contact(new Contact()
                                                                .name(contactName)
                                                                .url(contactUrl)
                                                                .email(contactEmail)));
        }

        @Bean
        public GroupedOpenApi groupedOpenApi() {
                return GroupedOpenApi.builder()
                                .group("📚 Buchbeschreibung & KI")
                                .packagesToScan("com.mybookstore.productintelligence.controller")
                                .build();
        }
}