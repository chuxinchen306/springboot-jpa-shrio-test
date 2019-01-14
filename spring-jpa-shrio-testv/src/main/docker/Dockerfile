FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD springboot-shrio-test-2.0.5.RELEASE.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]