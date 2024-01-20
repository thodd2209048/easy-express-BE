FROM maven:3.8.5-openjdk-17

VOLUME /target

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]