package com.mybookstore.productintelligence.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${WEB_DOMAIN:localhost}")
    private String webDomain;

    @Value("${server.port:8085}")
    private String serverPort;

    @Value("${FRONTEND_URL:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://" + webDomain,
                        "http://" + webDomain + ":" + serverPort,
                        "https://" + webDomain,
                        frontendUrl,
                        "http://localhost:8085", // Explizit für Swagger UI
                        "http://127.0.0.1:8085", // Alternative IP für lokale Entwicklung
                        "https://bookstore-demo.onrender.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}