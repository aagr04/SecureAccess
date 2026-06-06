# SecureAccess - Sistema de Gestion y Acceso de Usuarios

Sistema Full Stack para gestion de usuarios, login, roles, sesiones, menu dinamico, dashboard, carga masiva y mantenimiento de personas.

## Tecnologias y versiones

Backend:
- Java 17
- Spring Boot 3.3.5
- Maven
- Spring Security
- JWT con JJWT 0.12.6
- Spring Data JPA / Hibernate
- PostgreSQL
- Apache POI y OpenCSV para carga masiva
- Springdoc OpenAPI

Frontend:
- React 18.3.1
- Vite 5.4.x
- TypeScript 5.6.x
- Axios
- React Router DOM 6.28.x
- Vitest

Base de datos:
- PostgreSQL local
- PL/pgSQL
- Extension `pgcrypto`

Herramientas:
- Postman
- Git / GitHub

## Arquitectura

El backend usa arquitectura hexagonal:
- `domain`: modelos, puertos y reglas de dominio.
- `application`: servicios de aplicacion y casos de uso.
- `infrastructure`: seguridad, configuracion, controllers REST y adaptadores de persistencia.
- `ports`: contratos de entrada y salida.
- `adapters`: web y persistencia JPA.

La solucion mantiene separacion de responsabilidades, inyeccion de dependencias y reglas de negocio fuera de controllers.

## Estructura del proyecto

```text
backend/
frontend/
DataBase Script/
postman/
README.md
.gitignore
```

## Requisitos previos

- Java 17
- Maven o Maven Wrapper incluido
- Node.js y npm
- PostgreSQL local
- Git
- Postman

No se utiliza Docker.

## Configuracion de base de datos

1. Abrir pgAdmin, psql o una herramienta equivalente.
2. Ejecutar `DataBase Script/01_create_database.sql`.
3. Conectarse a `loginapp_db`.
4. Ejecutar `DataBase Script/02_schema.sql`.
5. Ejecutar `DataBase Script/03_functions.sql`.
6. Ejecutar `DataBase Script/04_seed_data.sql`.
7. Ejecutar `DataBase Script/05_test_queries.sql` para validar.

El archivo `DataBase Script/README_DATABASE.md` contiene el detalle de ejecucion.

## Configuracion backend

Archivo:

```text
backend/src/main/resources/application.properties
```

Configuracion local esperada:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/loginapp_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Si tu PostgreSQL usa otra clave, cambia `spring.datasource.password`.

## Levantar backend

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
cd backend
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

Backend:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Levantar frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend:

```text
http://localhost:5173
```

## Credenciales de prueba

ADMIN:
- Username: `Admin1234`
- Password: `Admin@1234`
- Email: `padmin@mail.com`

USER:
- Username: `User1234`
- Password: `User@1234`
- Email: `puser@mail.com`

Los usuarios seed se crean con hashes BCrypt desde PostgreSQL usando `pgcrypto`.

## Postman

1. Abrir Postman.
2. Importar `postman/SecureAccess.postman_collection.json`.
3. Importar `postman/SecureAccess.local.postman_environment.json`.
4. Seleccionar environment `SecureAccess Local`.
5. Ejecutar `Auth / Login ADMIN con username`.
6. El token se guarda automaticamente en la variable `token`.
7. Probar los demas endpoints.

## Endpoints principales

Auth:
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `POST /api/auth/recover`

Usuarios:
- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `GET /api/usuarios/me`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `PUT /api/usuarios/me`
- `PATCH /api/usuarios/{id}/estado`
- `DELETE /api/usuarios/{id}`
- `GET /api/usuarios/filter`
- `POST /api/usuarios/bulk`

Personas:
- `GET /api/personas`
- `GET /api/personas/{id}`
- `POST /api/personas`
- `PUT /api/personas/{id}`
- `DELETE /api/personas/{id}`

Roles:
- `GET /api/roles`
- `GET /api/roles/{id}`
- `POST /api/roles`
- `PUT /api/roles/{id}`
- `DELETE /api/roles/{id}`

Opciones y menu:
- `GET /api/opciones`
- `GET /api/opciones/{id}`
- `POST /api/opciones`
- `PUT /api/opciones/{id}`
- `DELETE /api/opciones/{id}`
- `GET /api/rol-opciones`
- `GET /api/rol-opciones/{id}`
- `POST /api/rol-opciones`
- `PUT /api/rol-opciones/{id}`
- `DELETE /api/rol-opciones/{id}`
- `GET /api/menu`

Sesiones:
- `GET /api/sessions`
- `GET /api/sessions/{idUsuario}`
- `GET /api/sessions/me/last`

Dashboard:
- `GET /api/dashboard/resumen`
- `GET /api/dashboard/sesiones-fallidas`

## Funciones de base de datos

Archivo:

```text
DataBase Script/03_functions.sql
```

Funciones:
- `fn_menu_por_rol(p_id_rol BIGINT)`
- `fn_intentos_fallidos_usuario(p_id_usuario BIGINT)`

## Reglas principales

- El correo de usuario se genera desde backend.
- El dominio de correo generado es `@mail.com`.
- Login con username o correo.
- Tres intentos fallidos bloquean el usuario.
- Solo una sesion activa por usuario.
- `ADMIN` puede editar usuarios `USER`.
- `ADMIN` no puede editar ni eliminar otro `ADMIN`.
- `USER` solo edita sus propios datos.
- Carga Excel/CSV solo para `ADMIN`.
- Menu obtenido desde base de datos segun rol.
- Eliminaciones logicas mediante campos `activo` y `status`.
- Al eliminar logicamente un usuario, la persona asociada se inactiva si no tiene otro usuario activo.

## Pruebas

Backend:

```bash
cd backend
./mvnw test
```

Windows PowerShell:

```powershell
cd backend
.\mvnw.cmd test
```

Frontend:

```bash
cd frontend
npm run build
npm test
```

En PowerShell, si `npm.ps1` esta bloqueado por politica de ejecucion, usar:

```powershell
npm.cmd run build
npm.cmd test
```

## Notas

- No se utiliza Docker.
- La base de datos se levanta localmente en PostgreSQL.
- `spring.jpa.hibernate.ddl-auto=validate`, por lo que las tablas deben existir antes de levantar el backend.
