FROM openjdk:11
EXPOSE 8001
COPY target/BBW-Rest-App.jar BBW-Rest-App.jar
ENTRYPOINT ["java","-jar","/BBW-Rest-App.jar"]