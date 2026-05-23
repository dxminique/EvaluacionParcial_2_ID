# Ticket Service — EP2 DOY0101

Microservicio de gestión de tickets con pipeline CI/CD completo implementado en GitHub Actions, usando contenedores Docker, análisis de seguridad y despliegue automatizado en AWS.

---

## Arquitectura del pipeline

```
Push/PR → Build & Tests → Snyk + SonarCloud → Docker Build → ECR Push → Deploy EC2
```

### Etapas del pipeline

| Etapa | Herramienta | Propósito |
|---|---|---|
| Build y Tests | Maven + JaCoCo | Compilación y cobertura ≥60% |
| Seguridad | Snyk | Escaneo de dependencias vulnerables |
| Calidad de código | SonarCloud | Análisis estático, code smells, bugs |
| Dependencias | Dependabot | PRs automáticos con actualizaciones |
| Contenedor | Docker multi-stage | Imagen optimizada y segura |
| Registro | AWS ECR | Almacenamiento de imágenes Docker |
| Deploy | EC2 + Docker Compose | Entorno cloud simulado |

---

## Trazabilidad

Cada commit genera una imagen Docker etiquetada con el SHA del commit (`${{ github.sha }}`), lo que permite identificar exactamente qué versión del código está desplegada en producción.

El pipeline **bloquea el deploy** si:
- Los tests fallan
- Snyk detecta vulnerabilidades de severidad `high` o superior
- SonarCloud reporta el Quality Gate como fallido

---

## Orquestación de contenedores (Docker Compose)

```yaml
services:
  mysql   → Base de datos con healthcheck
  ticket  → Microservicio (depende de mysql healthy)
```

Levantamiento local:
```bash
docker compose up -d
```

---

## Secrets requeridos en GitHub

| Secret | Descripción |
|---|---|
| `SNYK_TOKEN` | Token de autenticación Snyk |
| `SONAR_TOKEN` | Token de SonarCloud |
| `SONAR_ORGANIZATION` | Organización en SonarCloud |
| `AWS_ACCESS_KEY_ID` | Credenciales AWS |
| `AWS_SECRET_ACCESS_KEY` | Credenciales AWS |
| `EC2_HOST` | IP pública de la instancia EC2 |
| `EC2_USER` | Usuario SSH (ej: `ec2-user`) |
| `EC2_SSH_KEY` | Clave privada SSH |
| `ECR_REGISTRY` | URL del registro ECR |

---

## Garantía de calidad

- **Pruebas unitarias** con JUnit 5 + Mockito (sin dependencia de base de datos real)
- **Cobertura mínima** del 60% validada por JaCoCo en cada ejecución
- **Análisis de dependencias** automático cada lunes vía Dependabot
- **Análisis de código** en cada push vía SonarCloud

---

## Uso de IA

Este proyecto utilizó Claude (Anthropic) como apoyo para la generación de archivos de configuración (Dockerfile, docker-compose.yml, workflows). Todas las decisiones técnicas, ajustes y validaciones fueron realizadas por el equipo. Citado según política DuocUC: https://bibliotecas.duoc.cl/ia
