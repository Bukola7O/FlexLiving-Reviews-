       # Use an official Java runtime as a parent image
       FROM openjdk:17-jdk-slim

       # Set the working directory
       WORKDIR /app

       # Copy the Maven build files first
       COPY pom.xml .
       COPY src ./src

       # Package the Spring Boot app
       RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

       # Copy the built jar file
       COPY target/reviews-1.0.0.jar app.jar

       # Expose port 8080
       EXPOSE 8080

       # Run the application
       ENTRYPOINT ["java", "-jar", "app.jar"]
