FROM openjdk:21-jdk
WORKDIR /app
COPY target/Bot-0.0.1-SNAPSHOT.jar CategoryBot.jar
CMD ["java", "-jar", "CategoryBot.jar"]