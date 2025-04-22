# ---------- Stufe 1: Build mit Maven ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Nur pom.xml zuerst für besseren Caching-Effekt
COPY pom.xml .
RUN mvn dependency:go-offline

# Dann den restlichen Code kopieren
COPY src ./src

# Projekt bauen, Tests überspringen
RUN mvn clean package -DskipTests

# ---------- Stufe 2: Runtime ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Nur das JAR-File aus dem Build übernehmen
COPY --from=builder /app/target/book-service-*.jar app.jar

# Port für den Book-Service
EXPOSE 8082

# Startbefehl
ENTRYPOINT ["java", "-jar", "app.jar"]
