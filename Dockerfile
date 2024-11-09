FROM maven:3.8-openjdk-11-slim AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/mvp-hotel-booking.jar /app/mvp-hotel-booking.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/mvp-hotel-booking.jar"]
