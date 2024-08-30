FROM maven:3-eclipse-temurin-22-alpine AS BUILD

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:22-jdk

WORKDIR /app

COPY --from=BUILD /app/target/champions-club-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]