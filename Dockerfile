FROM maven:3.9.11-eclipse-temurin-21-alpine@sha256:922927df2c662cdd47ddb116443d6bec4696cfae3de1a0ddac8fcc7b87ce61ae AS builder
WORKDIR /build
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.22@sha256:326837fba06a8ff5482a17bafbd65319e64a6e997febb7c85ebe7e3f73c12b11 AS layer
WORKDIR /layer
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.14.0/opentelemetry-javaagent.jar .
COPY --from=builder /build/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine-3.22@sha256:326837fba06a8ff5482a17bafbd65319e64a6e997febb7c85ebe7e3f73c12b11
WORKDIR /app
RUN addgroup -S appuser && adduser -S -s /usr/sbin/nologin -G appuser appuser
COPY --from=layer /layer/opentelemetry-javaagent.jar ./
COPY --from=layer /layer/dependencies/ ./
COPY --from=layer /layer/spring-boot-loader/ ./
COPY --from=layer /layer/snapshot-dependencies/ ./
COPY --from=layer /layer/application/ ./
RUN chown -R appuser:appuser /app
USER appuser
HEALTHCHECK --interval=30s --timeout=3s --retries=1 CMD wget -q0- http://localhost:8080/actuator/health/ | grep UP || exit 1
ENV JAVA_TOOL_OPTIONS="-javaagent:./opentelemetry-javaagent.jar -Dotel.resource.attributes=service.name=demoapp-api"
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
