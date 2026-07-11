# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/department-service-*.jar department-service.jar

# Expose the port the application runs on
EXPOSE 8090

# Command to run the application
ENTRYPOINT ["java", "-jar", "department-service.jar"]