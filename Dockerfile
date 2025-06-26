# --- Build Stage ---
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# --- Runtime Stage ---
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]