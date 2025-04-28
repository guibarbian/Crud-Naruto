FROM amazoncorretto:17-alpine-jdk
LABEL authors="db"
COPY target/crud-naruto-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]