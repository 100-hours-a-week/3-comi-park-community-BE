# Build Stage
FROM eclipse-temurin:21-alpine AS builder

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew clean build -x test --no-daemon

# Run Stage
FROM bellsoft/liberica-runtime-container:jre-21-slim-musl

WORKDIR /app

COPY --from=builder /app/build/libs/community-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

CMD ["java", "-jar", "app.jar"]