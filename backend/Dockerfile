# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Cache Maven dependencies before copying source
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# Copy full source and build the fat JAR
COPY src ./src
RUN mvn clean package -DskipTests -B -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user for security
RUN addgroup -S bloop && adduser -S bloop -G bloop

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Make the jar owned by the non-root user
RUN chown bloop:bloop app.jar

USER bloop

EXPOSE 8080

# JAVA_OPTS lets Render inject memory flags (-Xmx400m -Xms128m)
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
