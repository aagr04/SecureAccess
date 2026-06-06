# Base de datos - Login App

Scripts ordenados para PostgreSQL local. No se usa Docker.

## Orden de ejecucion

1. Ejecutar `01_create_database.sql` conectado a una base administrativa, por ejemplo `postgres`.
2. Conectarse a `loginapp_db`.
3. Ejecutar `02_schema.sql`. Este script crea la extension `pgcrypto`, tablas, constraints e indices.
4. Ejecutar `03_functions.sql`.
5. Ejecutar `04_seed_data.sql`.
6. Ejecutar `05_test_queries.sql` para validar datos iniciales y funciones.

## Credenciales iniciales

ADMIN:
- Username: `Admin1234`
- Password: `Admin@1234`
- Email: `padmin@mail.com`

USER:
- Username: `User1234`
- Password: `User@1234`
- Email: `puser@mail.com`

## Funciones incluidas

- `fn_menu_por_rol(p_id_rol BIGINT)`: devuelve opciones activas del menu segun el rol.
- `fn_intentos_fallidos_usuario(p_id_usuario BIGINT)`: cuenta intentos fallidos registrados en sesiones.

## Compatibilidad con backend

Los nombres de tablas y columnas coinciden con las entidades JPA del backend:
`persona`, `usuarios`, `rol`, `rol_usuario`, `opciones`, `rol_opciones` y `sesiones`.

`usuarios.id_persona` es `UNIQUE`, por lo que una persona solo puede estar asociada a una cuenta de usuario.

Los passwords del seed se generan con `crypt(..., gen_salt('bf'))`, formato BCrypt compatible con `BCryptPasswordEncoder`.
