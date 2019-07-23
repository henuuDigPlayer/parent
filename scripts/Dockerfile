FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD activity-server.jar activity-server.jar
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/activity-server.jar"]
