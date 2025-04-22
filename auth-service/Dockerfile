# ---------- Stufe 1: Build mit Maven ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Nur pom.xml zuerst – für Layer-Caching der Abhängigkeiten
COPY pom.xml .
RUN mvn dependency:go-offline

# Danach den Quellcode kopieren
COPY src ./src

# Baue das Projekt
RUN mvn clean package -DskipTests

# ---------- Stufe 2: Runtime-Image ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Kopiere das erzeugte JAR aus der Build-Stufe
COPY --from=builder /app/target/auth-service-*.jar app.jar

# Port des Auth-Service
EXPOSE 8081

# Starte die Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]
