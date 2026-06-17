# LoginApp Full Stack

Aplicacion full stack con backend Java 17/Spring Boot y frontend React/TypeScript/Vite.

El proyecto incluye:

- Backend con arquitectura hexagonal, Spring Security, JWT en cookie `HttpOnly` y Redis para invalidar tokens por logout.
- Frontend React con Axios `withCredentials`, rutas protegidas, roles, menu dinamico y Tailwind CSS.
- PostgreSQL, Redis, backend y frontend levantados con Docker Compose.

Para ejecutar el entorno completo:

```bash
docker compose up --build
```

Credenciales por defecto:

- ADMIN: `AdminViamatica500` / `AdminViamatica@500`
- USER: `UserViamatica500` / `UserViamatica@500`

Documentacion especifica:

- [README_DOCKER.md](README_DOCKER.md)
- [backend/README_BACKEND.md](backend/README_BACKEND.md)
- [frontend/README_FRONTEND.md](frontend/README_FRONTEND.md)
