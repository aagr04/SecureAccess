# Docker

## Requisitos

- Docker Desktop instalado y en ejecucion.
- Puertos locales libres: `5173`, `8080`, `5432`, `6379`.

## Levantar todo

Desde la raiz del repositorio:

```bash
docker compose up --build
```

Servicios:

- Frontend: http://localhost:5173
- Backend: http://localhost:8080/api
- PostgreSQL: `localhost:5432`
- Redis: `localhost:6379`

## Detener

```bash
docker compose down
```

## Reinicio limpio

Docker Compose ejecuta el servicio `db-seed` en cada arranque para refrescar schema, funciones y credenciales obligatorias de forma idempotente. Si quieres recrear toda la base desde cero:

```bash
docker compose down -v
docker compose up --build
```

## Credenciales

ADMIN:

- Usuario: `AdminViamatica500`
- Email: `padmin@mail.com`
- Password: `AdminViamatica@500`

USER:

- Usuario: `UserViamatica500`
- Email: `puser@mail.com`
- Password: `UserViamatica@500`

## Variables principales

El archivo `.env` define valores locales:

- `POSTGRES_DB=loginapp_db`
- `POSTGRES_USER=postgres`
- `POSTGRES_PASSWORD=postgres`
- `SPRING_PROFILES_ACTIVE=docker`
- `JWT_SECRET`: clave HMAC de al menos 256 bits.
- `JWT_EXPIRATION=86400000`
- `APP_SECURITY_COOKIE_SECURE=false`
- `APP_SECURITY_COOKIE_SAME_SITE=Lax`
- `APP_CORS_ALLOWED_ORIGINS=http://localhost:5173,http://127.0.0.1:5173`

Para produccion HTTPS usar:

- `APP_SECURITY_COOKIE_SECURE=true`
- `APP_SECURITY_COOKIE_SAME_SITE=None`
- `APP_CORS_ALLOWED_ORIGINS=https://dominio-frontend`

## Cookies y Redis

El login crea la cookie `ACCESS_TOKEN` con `HttpOnly`, `Path=/`, `SameSite` configurable y `Secure` configurable. El JWT no se guarda en `localStorage` ni `sessionStorage`.

El logout lee la cookie, extrae el `jti` del JWT, lo guarda en Redis con TTL igual al tiempo restante del token y elimina la cookie. Cualquier request posterior con ese token debe responder `401`.

## Errores comunes

- Si necesitas limpiar datos antiguos de prueba, ejecutar `docker compose down -v` y volver a levantar.
- Si el navegador muestra sesion invalida despues de cambios de JWT, borrar cookies de `localhost`.
- Si Docker no puede conectar al daemon en Windows, ejecutar Docker Desktop o usar una terminal con permisos suficientes.
