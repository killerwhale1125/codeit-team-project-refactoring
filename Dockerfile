# 1. Base image: OpenJDK 17 with Alpine Linux for lightweight build
FROM openjdk:17-jdk-slim as Builder

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle

# 4. Copy source code
COPY src src

# 5. Build the Spring Boot application
RUN ./gradlew bootJar --no-daemon

# 6. Runtime image: Use lightweight JRE for running the application
FROM openjdk:17-jdk-slim

# 7. Set the working directory
WORKDIR /app

# 8. Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# 9. Expose the application port
EXPOSE 8080

# 10. Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]