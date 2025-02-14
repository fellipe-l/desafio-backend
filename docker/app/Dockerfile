FROM eclipse-temurin:21-jdk-alpine
RUN mkdir /opt/app
COPY target/desafio-0.0.1-SNAPSHOT.jar /opt/app
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.12.0/opentelemetry-javaagent.jar /opt/opentelemetry-agent.jar
ENTRYPOINT java -javaagent:/opt/opentelemetry-agent.jar -Dotel.resource.attributes=service.instance.id=$HOSTNAME -jar /opt/app/desafio-0.0.1-SNAPSHOT.jar
