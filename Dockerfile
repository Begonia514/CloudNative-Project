FROM openjdk:11

COPY ./target/hello-service-0.0.1-SNAPSHOT.jar /app/hello-service.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "hello-service.jar"]
