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

El `pom.xml` ya trae las propiedades de Sonar (`sonar.projectKey` y `sonar.organization: william-cabrera-devops-webinar`, `sonar.host.url`).

> Nota: el análisis se hace vía CI (GitHub Actions), por lo que "Automatic Analysis" debe estar deshabilitado en la configuración del proyecto en SonarCloud (Administration → Analysis Method); ambos modos no pueden estar activos a la vez.

```bash
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN
```

En SonarCloud, la rama principal del proyecto debe estar configurada como `main` (Project Settings → Branches → `main` → *Set as Main Branch*), ya que el repositorio no usa `master`.

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

El workflow en `.github/workflows/pipeline.yml` corre en cada push/PR a `main`, organizado en etapas:

| Etapa | Job(s) | Qué hace |
|---|---|---|
| Source | `source` | Resuelve aplicación, ambiente (según rama/evento) y versión (git sha) |
| Build & Test | `build-test` | Unit tests + integration tests (Failsafe, `*IT`) con gate mínimo de cobertura (JaCoCo), y API testing black-box contra el jar empaquetado |
| Quality & Security | `sonarqube` | Análisis SonarCloud + espera del Quality Gate |
| Quality & Security | `sast` | Análisis estático con CodeQL |
| Quality & Security | `secrets-scan` | Detección de secretos con Gitleaks |
| Package & Publish | `package-publish` | Build de la imagen Docker, escaneo de vulnerabilidades (Trivy, bloquea CRITICAL/HIGH) y push a GitHub Container Registry (GHCR) |
| Deploy & Validate | `deploy` | SSH a EC2, `docker pull` de la imagen ya publicada y `docker run` (timeout 5 min), health check con reintentos |
| Deploy & Validate | `dast` | Escaneo dinámico con OWASP ZAP (baseline) contra la app ya desplegada |
| Governance & Monitoring | `report` | Resumen de resultado de cada etapa en el Job Summary de GitHub Actions |

`package-publish`, `deploy` y `dast` solo corren en push a `main` (no en pull requests).

El job `deploy` usa un **GitHub Environment** llamado `production` — si configuras revisores requeridos en `Settings → Environments → production`, el pipeline pedirá aprobación manual antes de desplegar.

Secrets requeridos en el repositorio (Settings → Secrets and variables → Actions):

- `SONAR_TOKEN`
- `EC2_SSH_KEY` (llave privada)
- `EC2_HOST` (IP/DNS **pública** de la instancia)
- `EC2_USER`

`GITHUB_TOKEN` (automático) se usa para publicar en GHCR y para que el EC2 haga `docker login` al momento de pull.

El Security Group de la instancia EC2 debe permitir tráfico entrante en el puerto `80` (el contenedor mapea `80:8080`).

**Riesgo aceptado:** el health check y el DAST acceden por `http://` (sin TLS), ya que la demo no tiene dominio/certificado propio. Marcado como "Won't Fix" en SonarCloud.
