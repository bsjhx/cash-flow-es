# Use a lightweight JDK 21 base image for building
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copy Gradle-related files separately for caching
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

# Copy the rest of the application source
COPY src src
RUN ./gradlew bootJar --no-daemon

# Use a minimal runtime image
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
