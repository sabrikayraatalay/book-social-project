# --- Stage 1: Build the application using Maven ---
# Specify the platform for the build stage
FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file first to leverage Docker layer caching for dependencies
COPY pom.xml .

# Download project dependencies (only if pom.xml changes)
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Build the application, creating the executable JAR file. Skip tests during build.
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final lightweight application image ---
# Specify the platform for the final stage
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file created in the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on (default for Spring Boot is 8080)
EXPOSE 8080

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]