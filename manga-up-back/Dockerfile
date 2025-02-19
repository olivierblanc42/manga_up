# Utilisation de l'image OpenJDK
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR de l'application (assure-toi d'avoir déjà build ton projet avec `mvn package`)
COPY target/*.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
