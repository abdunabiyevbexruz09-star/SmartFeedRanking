FROM eclipse-temurin:17-jre
WORKDIR /app

# App JAR
COPY build/libs/SmartFeedRanking.jar app.jar

# Liquibase changelogs
COPY src/main/resources/db /db
COPY src/main/resources/banner /banner


EXPOSE 8083

# Run app
CMD ["java", "-jar", "app.jar"]