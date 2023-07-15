FROM openjdk:11
COPY target/icritic-users-service-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]