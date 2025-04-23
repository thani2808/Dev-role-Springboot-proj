FROM openjdk:17-jdk-alpine  
RUN apk add --no-cache curl
ADD target/privilege-0.0.1-SNAPSHOT.jar app.jar  
ENTRYPOINT ["java", "-jar", "/app.jar"]