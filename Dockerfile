
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runtime

# Configura variables de entorno
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Copia solo el jar desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto de la aplicación (ajústalo si usas otro en application.yml)
EXPOSE 80

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]
