# Despliegue en produccion

Guia para desplegar el backend en Railway, PostgreSQL en Railway, Redis compatible y frontend en Vercel.

## Backend en Railway

Configuracion del servicio:

```text
Root Directory: backend
Build Command: ./mvnw -DskipTests package
Start Command: java -jar target/loginapp-1.0.0.jar
```

Si Railway muestra error de permisos con `mvnw`, usar:

```text
Build Command: chmod +x mvnw && ./mvnw -DskipTests package
```

Variables de entorno:

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PORT/DATABASE
SPRING_DATASOURCE_USERNAME=USUARIO
SPRING_DATASOURCE_PASSWORD=PASSWORD
SPRING_DATA_REDIS_HOST=HOST_REDIS
SPRING_DATA_REDIS_PORT=6379
SPRING_DATA_REDIS_PASSWORD=
JWT_SECRET=CLAVE_LARGA_SEGURA
JWT_EXPIRATION=86400000
APP_CORS_ALLOWED_ORIGINS=https://frontend-publico.vercel.app
APP_SECURITY_COOKIE_SECURE=true
APP_SECURITY_COOKIE_SAME_SITE=None
```

Railway inyecta `PORT` automaticamente. La aplicacion usa `${PORT:8080}`.

Servicios requeridos:

- Backend Spring Boot.
- PostgreSQL.
- Redis o servicio compatible.

En Railway generar dominio publico desde:

```text
Settings -> Networking -> Generate Domain
```

## Base de datos en Railway PostgreSQL

Railway ya entrega una base creada. Normalmente no ejecutar:

```text
DataBase Script/01_create_database.sql
```

Ejecutar sobre la base real de Railway, en este orden:

```text
DataBase Script/02_schema.sql
DataBase Script/03_functions.sql
DataBase Script/04_seed_data.sql
```

El backend usa `spring.jpa.hibernate.ddl-auto=validate`, por lo que las tablas deben existir antes de iniciar el backend.

## Frontend en Vercel

Configuracion del proyecto:

```text
Root Directory: frontend
Build Command: npm run build
Output Directory: dist
```

Variable de entorno:

```text
VITE_API_BASE_URL=https://backend-en-railway.up.railway.app/api
```

El archivo `frontend/vercel.json` redirige rutas internas de React Router hacia `index.html` para evitar 404 al recargar.

## Validacion manual despues del despliegue

1. Abrir el frontend publicado.
2. Confirmar que no haya llamadas a `localhost` en la consola del navegador.
3. Iniciar sesion como administrador.
4. Confirmar que la cookie `ACCESS_TOKEN` se guarda como `HttpOnly`, `Secure` y `SameSite=None`.
5. Abrir dashboard y mantenimientos.
6. Probar logout.
7. Recargar rutas internas como `/dashboard` y `/usuarios`.
