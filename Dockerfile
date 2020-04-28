FROM openjdk:14-alpine
ARG JAR_FILE=target/degreedays.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
