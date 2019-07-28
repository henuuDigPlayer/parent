FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD zuul-server.jar zuul-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zuul-server.jar"]
