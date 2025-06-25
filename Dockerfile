FROM eclipse-temurin:17-jdk as build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

FROM eclipse-temurin:17-jdk
COPY --from=build app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["--server.port=8080", "--server.address=0.0.0.0"]
