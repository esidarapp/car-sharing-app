# Builder stage
FROM openjdk:21-jdk as builder
WORKDIR /car-sharing-app
ARG JAR_FILE=target/car-sharing-app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} car-sharing-app.jar
RUN java -Djarmode=layertools -jar car-sharing-app.jar extract

# Final stage
FROM openjdk:21-jdk
WORKDIR /car-sharing-app
COPY --from=builder /car-sharing-app/dependencies/ ./
COPY --from=builder /car-sharing-app/spring-boot-loader/ ./
COPY --from=builder /car-sharing-app/snapshot-dependencies/ ./
COPY --from=builder /car-sharing-app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
