package com.mybookstore.bookservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    @Value("${WEB_DOMAIN:*}")
    private String webDomain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Prüfen, ob ein spezifischer Wert gesetzt ist oder der Default-Wert
        if ("*".equals(webDomain)) {
            // Erlaube alle Ursprünge für die Entwicklung und Tests
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true);

            log.info("✅ [book-service] CORS aktiviert für alle Ursprünge");
        } else {
            // Verwende den konfigurierten Wert
            String httpOrigin = "http://" + webDomain;
            String httpsOrigin = "https://" + webDomain;

            registry.addMapping("/**")
                    .allowedOrigins(httpOrigin, httpsOrigin)
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true);

            log.info("✅ [book-service] CORS aktiviert für: {}, {}", httpOrigin, httpsOrigin);
        }
    }
}