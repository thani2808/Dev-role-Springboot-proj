FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/role-0.0.1-SNAPSHOT.jar app.jar

# Expose the intended port
EXPOSE 9004

# Entry point with port and binding options
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9004", "--server.address=0.0.0.0"]
