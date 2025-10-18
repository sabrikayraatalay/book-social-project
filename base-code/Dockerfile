# --- Stage 1: Build the application using Maven ---
# Dockerfile'ı kök dizinde çalıştırıyoruz.
FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-17 AS build

# Docker konteynerinin içindeki çalışma dizini
WORKDIR /app

# Kök pom.xml dosyasını kopyala (Gerekli olmasa da güvenlik için tutuyoruz)
COPY pom.xml .

# Uygulama pom.xml dosyasını kopyala
COPY base-code/pom.xml base-code/

# Uygulamanın kaynak kodunu kopyala
COPY base-code/src base-code/src

# Uygulamayı derle, testleri atla. Maven, base-code/pom.xml'i bulur ve derler.
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final lightweight application image ---
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# Docker konteynerinin içindeki çalışma dizini
WORKDIR /app

# JAR dosyasını 'build' aşamasından kopyala
COPY --from=build /app/base-code/target/*.jar app.jar

# Uygulamanın çalışacağı port
EXPOSE 8080

# Uygulamayı başlatma komutu
ENTRYPOINT ["java", "-jar", "app.jar"]