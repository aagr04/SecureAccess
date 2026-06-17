# Frontend LoginApp

Frontend React 18 + TypeScript + Vite para la prueba tecnica LoginApp.

## Tecnologias

- React 18
- TypeScript
- Vite
- Axios
- React Router DOM
- Context API
- Secure cookies con `withCredentials`
- Tailwind CSS
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
- `GET /api/auth/me`
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

## Autenticacion con cookies

- Axios usa `withCredentials: true`.
- El frontend no envia `Authorization: Bearer`.
- El JWT no se guarda en `localStorage` ni `sessionStorage`.
- El usuario y menu no sensibles se restauran con `GET /api/auth/me`.
- Un 401 limpia la sesion local y redirige a `/login`.
- Logout llama al backend y sincroniza otras pestanas con `BroadcastChannel`.

## Tailwind CSS

Configuracion:

```text
tailwind.config.js
postcss.config.js
src/styles/global.css
src/styles/layout.css
```

## Credenciales seed

ADMIN:

- username: `AdminViamatica500`
- email: `padmin@mail.com`
- password: `AdminViamatica@500`

USER:

- username: `UserViamatica500`
- email: `puser@mail.com`
- password: `UserViamatica@500`

## Pruebas

```bash
npm run build
npm test
```

La suite cubre login, rutas protegidas, roles, menu dinamico, migracion de ruta legacy de sesiones, dashboard, bulk upload, filtros y validaciones de usuario.
