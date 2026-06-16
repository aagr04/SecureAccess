# LoginApp Backend

Backend Java Spring Boot para login, usuarios, roles, menu dinamico, sesiones y dashboard administrativo.

## Tecnologias

- Java 17 obligatorio
- Spring Boot 3
- Maven Wrapper
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security
- JWT con `jjwt`
- Redis para blacklist de tokens por logout
- PostgreSQL
- Lombok
- Jakarta Validation
- Criteria Builder
- Apache POI para Excel
- OpenCSV/CSV basico
- Springdoc OpenAPI
- JUnit 5 / Mockito

## Arquitectura Hexagonal

La aplicacion separa dominio, casos de uso e infraestructura:

- `domain/model`: modelos puros sin Spring/JPA.
- `domain/port/in`: contratos de entrada o casos de uso.
- `domain/port/out`: contratos de salida hacia persistencia/funciones.
- `application/service`: implementacion de casos de uso y reglas de negocio.
- `infrastructure/adapter/in/web/api`: controllers REST de la capa Web API.
- `infrastructure/adapter/out/persistence`: entidades JPA, repositorios, adapters, mappers y Criteria Builder.
- `infrastructure/security`: JWT, filtro y Spring Security.
- `shared/dto`: requests/responses.

Los controllers no llaman a `JpaRepository`; consumen puertos de entrada. Los repositorios JPA viven en adapters de salida.

## Configurar PostgreSQL

Por defecto `src/main/resources/application.properties` usa:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/loginapp_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Ajustar usuario/password si tu PostgreSQL local usa otros valores.

## Configurar Redis y cookies

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=60000
app.security.cookie.secure=false
app.security.cookie.same-site=Lax
app.cors.allowed-origins=http://localhost:5173,http://127.0.0.1:5173
```

En produccion usar HTTPS:

```properties
app.security.cookie.secure=true
app.security.cookie.same-site=None
app.cors.allowed-origins=https://URL_FRONTEND_PUBLICO
```

## Ejecutar scripts

Desde la carpeta raiz del repositorio:

```bash
psql -U postgres -d postgres -f "DataBase Script/01_create_database.sql"
psql -U postgres -d loginapp_db -f "DataBase Script/02_schema.sql"
psql -U postgres -d loginapp_db -f "DataBase Script/03_functions.sql"
psql -U postgres -d loginapp_db -f "DataBase Script/04_seed_data.sql"
psql -U postgres -d loginapp_db -f "DataBase Script/05_test_queries.sql"
```

El compose refresca las credenciales por defecto en cada arranque para que un volumen previo no deje usuarios con passwords antiguos.

## Ejecucion local con Docker Compose

Desde la raiz del repositorio:

```bash
copy .env.example .env
docker compose up --build
```

Servicios incluidos:

- `backend`
- `frontend`
- `postgres`
- `redis`

El backend toma la base de datos, Redis, cookies seguras y CORS desde variables de entorno definidas para entorno `local/dev`.

## Abrir en IntelliJ IDEA

Abrir la carpeta `backend/` como proyecto Maven. Habilitar annotation processing para Lombok si IntelliJ lo solicita.

## Compilar

Windows:

```bat
cd backend
mvnw.cmd clean compile
```

Linux/Mac:

```bash
cd backend
./mvnw clean compile
```

## Tests

```bat
cd backend
mvnw.cmd clean test
```

## Levantar backend

```bat
cd backend
mvnw.cmd spring-boot:run
```

Swagger queda disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

## JWT con cookie segura

Rutas publicas:

- `POST /api/auth/login`
- `POST /api/auth/recover`

El login responde `Set-Cookie: ACCESS_TOKEN=...` con `HttpOnly`, `Path=/`, `SameSite` configurable y `Secure` configurable. El token no se expone en el body y el filtro JWT lee la cookie `ACCESS_TOKEN`.

Logout invalida el `jti` del JWT en Redis con TTL igual al tiempo restante del token y elimina la cookie del navegador.

Rutas ADMIN:

- `/api/dashboard/**`
- `/api/sessions/**`
- `PATCH /api/usuarios/{id}/estado`
- `POST /api/usuarios/bulk`

## Credenciales seed

ADMIN:

- username: `Admin1234`
- email: `padmin@mail.com`
- password: `AdminViamatica@500`

USER:

- username: `User1234`
- email: `puser@mail.com`
- password: `UserViamatica@500`

## Menu dinamico

El endpoint `GET /api/menu` devuelve rutas visuales para React Router. Para Sesiones debe devolver:

```json
{
  "nombre": "Sesiones",
  "ruta": "/sesiones",
  "icono": "clock"
}
```

El endpoint REST que consume esa pagina es `GET /api/sessions`.

## Usuarios de prueba

ADMIN:

- username: `Admin1234`
- password: `AdminViamatica@500`

USER:

- username: `User1234`
- password: `UserViamatica@500`

## Endpoints principales

- Auth: `/api/auth/login`, `/api/auth/logout`, `/api/auth/me`, `/api/auth/recover`
- Usuarios: `/api/usuarios`, `/api/usuarios/filter`, `/api/usuarios/bulk`
- Personas: `/api/personas`
- Roles: `/api/roles`
- Opciones: `/api/opciones`
- Rol opciones: `/api/rol-opciones`
- Menu: `/api/menu`
- Sesiones: `/api/sessions`
- Dashboard: `/api/dashboard/resumen`, `/api/dashboard/sesiones-fallidas`
