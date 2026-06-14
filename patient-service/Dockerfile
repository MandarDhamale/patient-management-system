FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre AS runner
WORKDIR /app
COPY --from=builder ./app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar", \
    "--spring.datasource.url=${SPRING_DATASOURCE_URL}", \
    "--spring.datasource.username=${SPRING_DATASOURCE_USERNAME}", \
    "--spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}"]