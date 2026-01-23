# Multi-stage Dockerfile for building and running the Spring Boot app
# Stage 1: build with Gradle (uses official Gradle image with JDK 17)
FROM gradle:7.6-jdk17 AS builder
WORKDIR /home/gradle/project

# Copy only the files required for dependency resolution first to leverage Docker cache
COPY settings.gradle build.gradle gradle.* ./
COPY gradlew ./

# Copy modules and sources needed for a multi-module build
COPY applications/app-service ./applications/app-service
COPY domain ./domain
COPY usecase ./usecase
COPY infrastructure ./infrastructure

# Make the Gradle wrapper executable and build the bootJar for the app-service module
RUN chmod +x ./gradlew && \
    ./gradlew :applications:app-service:bootJar -x test --no-daemon


# Stage 2: runtime image
FROM eclipse-temurin:17-jre-jammy
ARG JAR_GLOB=applications/app-service/build/libs/*.jar

# Create non-root user for safer runtime
RUN useradd --system --create-home appuser || true
WORKDIR /app

# Copy jar from builder stage (the bootJar produced by Gradle)
COPY --from=builder /home/gradle/project/${JAR_GLOB} /app/app.jar

# Expose HTTP port used by Spring Boot
EXPOSE 8080

# Recommended production JVM options can be passed via JAVA_OPTS
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Run as non-root
USER appuser

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
