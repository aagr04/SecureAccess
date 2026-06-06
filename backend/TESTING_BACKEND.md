# Testing Backend

## Comandos

```bat
cd backend
mvnw.cmd clean compile
mvnw.cmd clean test
```

Linux/Mac:

```bash
cd backend
./mvnw clean compile
./mvnw clean test
```

## Cobertura creada

Pruebas unitarias:

- `UsernameValidator`
- `PasswordValidator`
- `IdentificacionValidator`
- `EmailGeneratorService`
- `AuthApplicationService`
- `UsuarioApplicationService`
- `MenuApplicationService`
- `DashboardApplicationService`

Pruebas de integracion con `SpringBootTest` y `MockMvc`:

- Login correcto.
- Login incorrecto.
- Ruta protegida sin token devuelve 401.
- Dashboard con rol USER devuelve 403.
- Dashboard con rol ADMIN devuelve 200.
- Crear usuario no expone password.
- DELETE de usuario llama eliminacion logica.
- Menu autenticado devuelve opciones.
- Menu autenticado devuelve `Sesiones` con ruta `/sesiones` e icono `clock`.
- Filtro de usuarios no expone password.
- `GET /api/sessions` con ADMIN devuelve 200.
- `GET /api/sessions` con USER devuelve 403.

## Resultado de ultima ejecucion

```text
Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
