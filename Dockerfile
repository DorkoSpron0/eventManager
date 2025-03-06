FROM amazoncorretto:21-al2023

WORKDIR /app

COPY target/eventManagerRun-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8787

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]