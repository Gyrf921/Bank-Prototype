FROM openjdk:11-slim
LABEL authors="ShchinVV"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app.jar"]