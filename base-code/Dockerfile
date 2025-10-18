# --- Stage 1: Build the application using Maven ---
# Build aşaması için platformu belirtiyoruz (Mac M serisi için önemli)
FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-17 AS build

# Docker konteynerinin içindeki çalışma dizini
WORKDIR /app

# Dockerfile, base-code klasöründe olduğu için sadece pom.xml kopyalanır
COPY pom.xml .

# Uygulamanın kaynak kodunu kopyala (base-code/src yerine sadece src kullanılır)
COPY src ./src

# Uygulamayı derle, testleri atla.
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final lightweight application image ---
# Final imaj için JRE kullanıyoruz
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# Docker konteynerinin içindeki çalışma dizini
WORKDIR /app

# JAR dosyasını 'build' aşamasından kopyala (target klasörünü direkt kullanır)
COPY --from=build /app/target/*.jar app.jar

# Uygulamanın çalışacağı port
EXPOSE 8080

# Uygulamayı başlatma komutu
ENTRYPOINT ["java", "-jar", "app.jar"]