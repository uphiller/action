FROM openjdk:17-jdk-alpine
RUN  apk update && \
     apk add curl tzdata ttf-freefont && \
     cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime

COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=dev","-jar","/app.jar"]