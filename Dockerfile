#
## Use official OpenJDK image
#FROM eclipse-temurin:21-jdk
#
## Set working directory in container
#WORKDIR /app
#
## Copy the fat JAR into the container
#COPY build/libs/ecom-0.0.1-SNAPSHOT.jar app.jar
#
## Expose port your Spring Boot app runs on
#EXPOSE 8080
#
## Environment variables (optional defaults, can be overridden)
#ENV SPRING_DATASOURCE_URL=jdbc:postgresql://ep-dark-heart-adzouazi-pooler.c-2.us-east-1.aws.neon.tech:5432/raqaf-kwt
#ENV SPRING_DATASOURCE_USERNAME=neondb_owner
#ENV SPRING_DATASOURCE_PASSWORD=npg_3hGXeN1LFtPK
#
## Run the jar file
#ENTRYPOINT ["java", "-jar", "app.jar"]
### Stage 1: Build the JAR using Gradle
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# Build application (JAR will be inside /app/build/libs)
RUN gradle build -x test

### Stage 2: Run the JAR
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]