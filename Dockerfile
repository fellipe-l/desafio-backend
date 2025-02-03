FROM eclipse-temurin:21-jdk-alpine
RUN mkdir /opt/app
COPY ./target/desafio-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/desafio-0.0.1-SNAPSHOT.jar"]
