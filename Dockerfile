# ==========================
# ETAPA 1: Construcción del JAR
# ==========================
FROM eclipse-temurin:21-jdk AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el archivo pom y descargar dependencias primero (mejora el cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Copiar el resto del código y compilar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==========================
# ETAPA 2: Imagen de ejecución
# ==========================
FROM eclipse-temurin:21-jre

# Directorio de la app
WORKDIR /app

# Copiar el JAR generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto (Fly.io lo detectará automáticamente)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
