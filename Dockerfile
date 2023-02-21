# Set the base image to use
FROM openjdk:17

# Add a volume pointing to /tmp
VOLUME /tmp

# Set the working directory
WORKDIR /app

# Added for docker build gitlabd-url approach
#RUN mvn -f pom.xml clean package -DskipTests

# Copy the JAR file into the container at /app
#COPY target/mongoflux-0.0.1-SNAPSHOT.jar app.jar
COPY target/mongoflux.jar /app/mongoflux.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]