# ==========================
# ETAPA 1: Construcción del JAR
# ==========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos los archivos de Maven Wrapper
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Damos permisos de ejecución al mvnw (esto corrige tu error)
RUN chmod +x mvnw

# Descargamos dependencias (usa el wrapper con permisos)
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos el proyecto
RUN ./mvnw clean package -DskipTests

# ==========================
# ETAPA 2: Imagen final ligera
# ==========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
