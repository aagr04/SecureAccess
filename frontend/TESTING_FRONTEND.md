# Pruebas manuales frontend

Ejecutar el backend en `http://localhost:8080/api`, configurar `.env` con `VITE_API_BASE_URL=http://localhost:8080/api` y levantar el frontend con `npm run dev`.

| # | Caso | Resultado esperado |
|---|------|--------------------|
| 1 | Login con username ADMIN | Ingresa correctamente y redirige a `/bienvenida`. |
| 2 | Login con email ADMIN | Ingresa correctamente y conserva token, usuario y rol. |
| 3 | Login con password incorrecta | Muestra error controlado y no guarda sesión. |
| 4 | Login con usuario bloqueado | Muestra mensaje de usuario bloqueado. |
| 5 | Login con sesión activa | Muestra mensaje de sesión activa existente. |
| 6 | Recover password | Valida campo requerido y muestra mensaje del backend. |
| 7 | Menú ADMIN | Renderiza opciones administrativas devueltas por `/menu`. |
| 8 | Menú USER | Renderiza solo opciones permitidas por backend. |
| 9 | Dashboard ADMIN | Carga indicadores desde `/dashboard/resumen` y sesiones fallidas. |
| 10 | Dashboard bloqueado para USER | Redirige a `/access-denied`. |
| 11 | Listar usuarios | ADMIN visualiza tabla con usuarios desde `/usuarios`. |
| 12 | Filtrar usuarios | ADMIN filtra por nombres, apellidos, identificación, username, email, status y rol. |
| 13 | Crear usuario | Valida campos y crea usando `POST /usuarios`. |
| 14 | Editar usuario | Carga datos en formulario y actualiza usando `PUT /usuarios/{id}`. |
| 15 | Cambiar estado usuario | ADMIN cambia estado con `PATCH /usuarios/{id}/estado`. |
| 16 | Carga masiva archivo válido | Acepta `.xlsx`, `.xls` o `.csv` y muestra totales del backend. |
| 17 | Carga masiva archivo inválido | Bloquea extensión inválida antes de llamar API. |
| 18 | Logout | Consume `/auth/logout`, limpia sesión y vuelve a `/login`. |
| 19 | Token expirado | Interceptor limpia sesión y redirige a `/login` ante 401. |
| 20 | Backend apagado | Muestra error de conexión sin romper la aplicación. |

## Pruebas automatizadas

```bash
npm test
```

Cobertura incluida:

- Login renderiza, valida campos, muestra error backend y redirige tras login exitoso.
- RecoverPasswordPage renderiza.
- ProtectedRoute bloquea rutas privadas sin token.
- RoleRoute bloquea USER en dashboard.
- Menú dinámico renderiza rutas del backend.
- UsuarioBulkUpload valida extensión.
- Dashboard muestra indicadores.
- UsuarioFilters elimina filtros vacíos.
- Logout limpia estado y storage.
- UsuarioTable oculta acciones administrativas para USER.

## Validacion final Sesiones

- ADMIN: click en `Sesiones` navega a `/sesiones` y consume `GET /api/sessions`.
- USER: no recibe `Sesiones` en el menu; acceso manual a `/sesiones` muestra `/access-denied`.
- Compatibilidad: menus antiguos guardados con `/sessions` se migran a `/sesiones`.
- Ultima ejecucion: 12 archivos de prueba, 16 tests OK.
