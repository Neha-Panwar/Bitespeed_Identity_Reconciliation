FROM openjdk:11
EXPOSE 8000
COPY ./target/contact-identity-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]