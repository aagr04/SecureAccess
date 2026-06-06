# Frontend LoginApp

Frontend React 18 + TypeScript + Vite para la prueba tecnica LoginApp.

## Tecnologias

- React 18
- TypeScript
- Vite
- Axios
- React Router DOM
- Context API
- JWT con `Authorization: Bearer TOKEN`
- Vitest y React Testing Library

## Abrir en Visual Studio Code

Abrir directamente la carpeta `frontend/`.

## Variables de entorno

Crear `.env` desde `.env.example` si hace falta:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Comandos

```bash
npm install
npm run dev
npm run build
npm test
```

En PowerShell, si `npm` esta bloqueado por Execution Policy, usar `npm.cmd`.

## Rutas frontend

- `/login`
- `/recover`
- `/bienvenida`
- `/dashboard` ADMIN
- `/usuarios` ADMIN
- `/personas` ADMIN
- `/roles` ADMIN
- `/opciones` ADMIN
- `/sesiones` ADMIN
- `/perfil`
- `/access-denied`
- `/404`

Alias de compatibilidad:

- `/sessions` redirige a `/sesiones` dentro de rutas ADMIN.

## Integracion backend

- `POST /api/auth/login`
- `POST /api/auth/logout`
- `POST /api/auth/recover`
- `GET /api/menu`
- `GET /api/usuarios`
- `GET /api/personas`
- `GET /api/roles`
- `GET /api/opciones`
- `GET /api/sessions`
- `GET /api/dashboard/resumen`
- `GET /api/dashboard/sesiones-fallidas`

Regla importante:

- Ruta visual React: `/sesiones`
- Endpoint REST: `/api/sessions`
- Menu PostgreSQL: `Sesiones -> ruta /sesiones, icono clock`

El storage normaliza menus antiguos con `/sessions` hacia `/sesiones`.

## Credenciales seed

ADMIN:

- username: `Admin1234`
- email: `padmin@mail.com`
- password: `AdminViamatica2026`

USER:

- username: `User1234`
- email: `puser@mail.com`
- password: `UserViamatica2026`

## Pruebas

```bash
npm run build
npm test
```

La suite cubre login, rutas protegidas, roles, menu dinamico, migracion de ruta legacy de sesiones, dashboard, bulk upload, filtros y validaciones de usuario.
