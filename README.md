# POC Employee Service (Java 21, Spring Boot 3) — MySQL + SSM (per env)

Microservicio de ejemplo listo para ECS (EC2) y RDS **MySQL**. Obtiene la configuración de BD desde
**AWS SSM Parameter Store** según el ambiente (`APP_ENV=dev|qa|prod`).

## Endpoints
- `GET /api/empleados` → lista `{ codigo, nombre, email }`
- `POST /api/empleados` → crea `{ codigo, nombre, email }`

## SSM parámetros esperados
Por defecto, `APP_SSM_PREFIX=/myapp` y `APP_ENV=dev` (puedes cambiarlos via env vars). La app leerá:
- `${APP_SSM_PREFIX}/${APP_ENV}/db/url`       (p. ej. `jdbc:mysql://my-rds:3306/appdb`)
- `${APP_SSM_PREFIX}/${APP_ENV}/db/username`  (usuario)
- `${APP_SSM_PREFIX}/${APP_ENV}/db/password`  (password, puede ser SecureString)

> Requisitos IAM para la **task role**: `ssm:GetParameter(s)*` (+ `kms:Decrypt` si usas SecureString con KMS).
  Ya lo agregaste en tu IaC.

## Variables de entorno principales
- `APP_ENV`           → `dev` (default), `qa`, `prod`
- `APP_SSM_PREFIX`    → `/myapp` (default)
- `APP_DB_FROM_SSM`   → `true` (default). Si la pones `false`, la app usará `SPRING_DATASOURCE_*` (útil en tests/local)
- `AWS_REGION`        → región de tus parámetros (necesario en ECS)
- (Opcional) `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` si `APP_DB_FROM_SSM=false`

## Ejecutar local (sin SSM, usando MySQL local)
```bash
export APP_DB_FROM_SSM=false
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/appdb
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=secret
./mvnw spring-boot:run
```

## Docker
```bash
docker build -t employee-service:local .
docker run --rm -p 8080:8080       -e APP_ENV=dev -e APP_SSM_PREFIX=/myapp -e APP_DB_FROM_SSM=true -e AWS_REGION=us-east-1       employee-service:local
```

## Build
```bash
./mvnw clean package -DskipTests
```

## Tests Unitarios
```bash
./mvnw test
```

## Tests de Integracion
```bash
./mvnw verify -DskipUnitTests=true
```


## GitHub Actions
Workflow en `.github/workflows/ci.yml` que construye y ejecuta tests (unit + integración).
