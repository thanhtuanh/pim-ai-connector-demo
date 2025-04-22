package com.mybookstore.productintelligence.config;

import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springdoc.core.customizers.OperationCustomizer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpringDocConfiguration {

    @Value("${server.port:8085}")
    private String serverPort;

    @Value("${WEB_DOMAIN:localhost}")
    private String webDomain;

    /**
     * Konfiguriere einen benutzerdefinierten OperationCustomizer,
     * um sicherzustellen, dass die Swagger-UI richtig geladen wird.
     */
    @Bean
    @Primary
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {
            // Stelle sicher, dass alle Operationen die richtigen Server verwenden
            if (operation.getServers() == null) {
                List<Server> servers = new ArrayList<>();
                servers.add(new Server()
                        .url("http://" + webDomain + ":" + serverPort)
                        .description("Local Development Server"));
                operation.setServers(servers);
            }
            return operation;
        };
    }
}