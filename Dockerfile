# Set the base image to use
FROM openjdk:17

# Add a volume pointing to /tmp
VOLUME /tmp

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container at /app:

# Local approach:
COPY target/mongoflux-0.0.1-SNAPSHOT.jar app.jar

# Url approach (e.g. docker build gitlab-url):
#COPY . app/mongoflux-0.0.1-SNAPSHOT.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
