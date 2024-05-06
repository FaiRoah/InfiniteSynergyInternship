FROM openjdk:22-jdk

COPY target/bankServer-0.0.1-SNAPSHOT.jar bankServer.jar

ENTRYPOINT ["java","-jar","/bankServer.jar"]
