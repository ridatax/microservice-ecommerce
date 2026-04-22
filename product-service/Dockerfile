# --- ETAPA 1: BUILD (La Construcción de la aplicación) ---
FROM gradle:9.4-jdk25-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Damos permisos de ejecución al wrapper
RUN chmod +x gradlew

# Copiamos el resto del código
COPY . .
# Construimos usando el wrapper (./gradlew) en lugar de 'gradle' directamente para asegurar que se use la versión correcta de Gradle definida en el proyecto.
# Además, usamos --no-daemon para evitar problemas con el daemon de Gradle en entornos de CI/CD.
RUN ./gradlew build -x test --no-daemon


# --- ETAPA 2: RUNTIME (La Ejecución de la aplicación) ---
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar product.jar
ENTRYPOINT ["java", "-jar", "product.jar"]
