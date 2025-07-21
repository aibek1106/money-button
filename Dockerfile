# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY pom.xml /workspace/
COPY src /workspace/src
WORKDIR /workspace
RUN mvn -q package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
