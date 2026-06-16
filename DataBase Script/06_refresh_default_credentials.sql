-- Refresca las credenciales por defecto en cada arranque local.
-- Esto evita que un volumen PostgreSQL reutilice usuarios viejos con passwords anteriores.

INSERT INTO usuarios (username, password, email, status, activo, intentos_fallidos, sesion_activa, id_persona)
SELECT 'AdminViamatica500', crypt('AdminViamatica@500', gen_salt('bf')), 'padmin@mail.com', 'ACTIVO', TRUE, 0, FALSE, p.id_persona
FROM persona p
WHERE p.identificacion = '0912345678'
ON CONFLICT (email) DO UPDATE SET
    username = EXCLUDED.username,
    password = EXCLUDED.password,
    status = EXCLUDED.status,
    activo = EXCLUDED.activo,
    intentos_fallidos = EXCLUDED.intentos_fallidos,
    sesion_activa = EXCLUDED.sesion_activa,
    id_persona = EXCLUDED.id_persona;

INSERT INTO usuarios (username, password, email, status, activo, intentos_fallidos, sesion_activa, id_persona)
SELECT 'UserViamatica500', crypt('UserViamatica@500', gen_salt('bf')), 'puser@mail.com', 'ACTIVO', TRUE, 0, FALSE, p.id_persona
FROM persona p
WHERE p.identificacion = '0923456789'
ON CONFLICT (email) DO UPDATE SET
    username = EXCLUDED.username,
    password = EXCLUDED.password,
    status = EXCLUDED.status,
    activo = EXCLUDED.activo,
    intentos_fallidos = EXCLUDED.intentos_fallidos,
    sesion_activa = EXCLUDED.sesion_activa,
    id_persona = EXCLUDED.id_persona;

INSERT INTO rol_usuario (id_usuario, id_rol, activo)
SELECT u.id_usuario, r.id_rol, TRUE
FROM usuarios u, rol r
WHERE u.email = 'padmin@mail.com'
  AND r.nombre = 'ADMIN'
ON CONFLICT (id_usuario, id_rol) DO NOTHING;

INSERT INTO rol_usuario (id_usuario, id_rol, activo)
SELECT u.id_usuario, r.id_rol, TRUE
FROM usuarios u, rol r
WHERE u.email = 'puser@mail.com'
  AND r.nombre = 'USER'
ON CONFLICT (id_usuario, id_rol) DO NOTHING;
