# ---------- Build-Phase ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Nur relevante Dateien kopieren für schnelleres Caching
COPY pom.xml .
COPY src ./src

# Optional: Abhängigkeiten separat cachen (schnellere Wiederbuilds)
# RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

# ---------- Runtime-Phase ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# JAR-Datei aus Build-Phase kopieren
COPY --from=build /app/target/product-intelligence-service-0.0.1-SNAPSHOT.jar app.jar

# Externe Configs (z. B. .env oder credentials) können gemountet werden
EXPOSE 8085

# Sichere und saubere Startweise
ENTRYPOINT ["java", "-jar", "app.jar"]
