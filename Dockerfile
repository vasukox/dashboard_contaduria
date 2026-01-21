# --- Etapa 1: Construcción con Maven ---
# Usamos una imagen oficial de Maven con Java 17 para construir el proyecto.
FROM maven:3.8.5-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copiamos solo el pom.xml para descargar las dependencias primero (cache de Docker).
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente.
COPY src ./src

# Construimos el proyecto y creamos el archivo .jar, saltando los tests.
RUN mvn package -DskipTests


# --- Etapa 2: Ejecución ---
# Usamos una imagen oficial y ligera de Eclipse Temurin con Java 17 (JRE).
# Esta es una alternativa más robusta a la anterior.
FROM eclipse-temurin:17-jre

# Establecemos el directorio de trabajo.
WORKDIR /app

# Copiamos el .jar construido desde la etapa anterior.
COPY --from=build /app/target/facturacion-factus-1.0-SNAPSHOT.jar app.jar

# Exponemos el puerto que usará la aplicación (Render lo gestionará).
EXPOSE 8080

# El comando para iniciar la aplicación cuando el contenedor se inicie.
ENTRYPOINT ["java", "-jar", "app.jar"]