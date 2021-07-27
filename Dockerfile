#
# taken from https://spring.io/guides/gs/spring-boot-docker/
#

#
# mvn spring-boot:build-image
#

FROM adoptopenjdk

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]