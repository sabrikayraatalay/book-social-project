# --- Stage 1: Build the application using Maven ---
# Specify platform for Apple Silicon (M1/M2) compatibility.
FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the pom.xml first to leverage Docker's layer caching for dependencies.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code.
COPY src ./src

# Compile the application and skip tests (tests will be run in CI, not here).
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final lightweight application image ---
# Specify platform for Apple Silicon (M1/M2) compatibility.
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the final .jar file from the 'build' stage.
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application will run on.
EXPOSE 8080

# The command to run the application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]