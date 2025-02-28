FROM maven:3.9.9-eclipse-temurin-21-alpine@sha256:4cbb8bf76c46b97e028998f2486ed014759a8e932480431039bdb93dffe6813e AS builder
WORKDIR /build
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21.0.6_7-jre-alpine@sha256:4e9ab608d97796571b1d5bbcd1c9f430a89a5f03fe5aa6c093888ceb6756c502 AS layer
WORKDIR /layer
COPY --from=builder /build/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21.0.6_7-jre-alpine@sha256:4e9ab608d97796571b1d5bbcd1c9f430a89a5f03fe5aa6c093888ceb6756c502
WORKDIR /app
RUN addgroup -S appuser && adduser -S -s /usr/sbin/nologin -G appuser appuser
COPY --from=layer /layer/dependencies/ ./
COPY --from=layer /layer/spring-boot-loader/ ./
COPY --from=layer /layer/snapshot-dependencies/ ./
COPY --from=layer /layer/application/ ./
RUN chown -R appuser:appuser /app
USER appuser
HEALTHCHECK --interval=30s --timeout=3s --retries=1 CMD wget -q0- http://localhost:8080/actuator/health/ | grep UP || exit 1
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
