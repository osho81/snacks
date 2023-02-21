# Set the base image to use
FROM adoptopenjdk:17-jre-hotspot

# Add a volume pointing to /tmp
VOLUME /tmp

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/mongoflux-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]