FROM openjdk:21-jdk-slim
COPY target/hacker-news-connector-1.0.0.jar app.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java","-jar","app.jar"]