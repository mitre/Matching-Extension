#
# taken from https://spring.io/guides/gs/spring-boot-docker/
#

#
# mvn spring-boot:build-image
#

FROM openjdk:18-slim-buster

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]