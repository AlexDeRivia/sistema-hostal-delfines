FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM ghcr.io/eclipse-ee4j/embedded-glassfish:7.1.0
COPY --from=build /app/target/HOSTAL_DELFINES_APRRV-1.0.war /deploy/application.war
ENV HOSTAL_DATA_DIR=/data
EXPOSE 8080
