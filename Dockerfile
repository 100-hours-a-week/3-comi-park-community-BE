# Build Stage
FROM amazoncorretto:21-alpine AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

# Run Stage
FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/community-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]