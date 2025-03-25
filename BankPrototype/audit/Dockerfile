FROM openjdk:17-slim
LABEL authors="ShchinVV"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "/app.jar"]