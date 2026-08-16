# moventi-webinar-devops

Aplicación de ejemplo (REST API) para el webinar de DevOps 2026.

## Stack

- Java 21
- Spring Boot 3.5.4 (Spring Web + Actuator)
- Maven
- JUnit 5
- JaCoCo (cobertura de código)
- SonarQube Cloud (SonarScanner for Maven)
- Docker

## Endpoint

```
GET /api/v1/hello?name={nombre}
```

`name` es opcional (por defecto `DevOps`).

Ejemplo de respuesta:

```json
{
  "message": "Hola DevOps",
  "application": "DevOps Webinar 2026",
  "version": "1.0.0",
  "timestamp": "2026-08-16T15:00:00Z"
}
```

También expone Actuator en `/actuator/health`.

## Requisitos previos

- JDK 21
- Maven 3.9+ (o usar el wrapper `mvnw` si lo agregas al proyecto)
- Docker (opcional, para contenedor)

## Levantar la aplicación localmente

### 1. Correr los tests

```bash
mvn clean test
```

Resultado esperado:

```
Tests run: 1, Failures: 0, Errors: 0

BUILD SUCCESS
```

### 2. Compilar y empaquetar

```bash
mvn clean package
```

Esto compila, corre los tests y genera el reporte de cobertura con JaCoCo en:

```
target/site/jacoco/index.html
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

o, luego de empaquetar:

```bash
java -jar target/moventi-webinar-devops-1.0.0.jar
```

La app queda escuchando en `http://localhost:8080`.

### 4. Probar el endpoint

```bash
curl "http://localhost:8080/api/v1/hello"
curl "http://localhost:8080/api/v1/hello?name=William"
```

## Análisis con SonarQube Cloud

El `pom.xml` ya trae las propiedades de Sonar (`sonar.projectKey`, `sonar.organization`, `sonar.host.url`). Antes de lanzar el análisis, reemplaza `TU_ORGANIZATION` por tu organización real de SonarCloud.

```bash
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN
```

## Levantar con Docker

### 1. Construir la imagen

```bash
docker build -t moventi-webinar-devops .
```

### 2. Ejecutar el contenedor

```bash
docker run -d \
  --name moventi-webinar-devops \
  -p 8080:8080 \
  moventi-webinar-devops
```

### 3. Probar

```bash
curl "http://localhost:8080/api/v1/hello?name=William"
```

## CI/CD

El workflow en `.github/workflows/pipeline.yml` corre en cada push/PR a `main`:

1. **build-and-test**: compila con Maven, corre los tests y genera el reporte JaCoCo (se publica como artifact de GitHub Actions).
2. **docker-build**: (solo en push a `main`) construye la imagen Docker y la publica en Docker Hub usando los secrets `DOCKERHUB_USERNAME` y `DOCKERHUB_TOKEN`.

Para que el segundo job funcione, configura esos dos secrets en el repositorio (Settings → Secrets and variables → Actions).
