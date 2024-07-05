FROM eclipse-temurin:21-jre

WORKDIR /app

COPY /target/batch-0.0.1-SNAPSHOT.jar /app/batch.jar

ENTRYPOINT ["java", "-jar", "/app/batch.jar", "--spring.profiles.active=prod"]