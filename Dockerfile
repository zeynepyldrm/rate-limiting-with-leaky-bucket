FROM openjdk:17-jdk-slim AS build
EXPOSE 8080

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x ./mvnw
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]