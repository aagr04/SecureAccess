-- Ejecutar conectado a loginapp_db.
-- Los hashes generados con pgcrypto/gen_salt('bf') son compatibles con BCryptPasswordEncoder.

INSERT INTO rol (nombre, activo) VALUES ('ADMIN', TRUE) ON CONFLICT (nombre) DO NOTHING;
INSERT INTO rol (nombre, activo) VALUES ('USER', TRUE) ON CONFLICT (nombre) DO NOTHING;

INSERT INTO opciones (nombre, ruta, icono, orden, activo) VALUES
('Dashboard', '/dashboard', 'layout-dashboard', 1, TRUE),
('Usuarios', '/usuarios', 'users', 2, TRUE),
('Personas', '/personas', 'id-card', 3, TRUE),
('Roles', '/roles', 'shield', 4, TRUE),
('Opciones', '/opciones', 'menu', 5, TRUE),
('Sesiones', '/sessions', 'clock', 6, TRUE),
('Bienvenida', '/bienvenida', 'home', 1, TRUE),
('Mi Perfil', '/perfil', 'user', 2, TRUE)
ON CONFLICT (nombre, ruta) DO NOTHING;

INSERT INTO persona (nombres, apellidos, identificacion, fecha_nacimiento, activo)
VALUES ('Persona', 'Admin', '0912345678', '1990-01-01', TRUE)
ON CONFLICT (identificacion) DO NOTHING;

INSERT INTO persona (nombres, apellidos, identificacion, fecha_nacimiento, activo)
VALUES ('Persona', 'User', '0923456789', '1995-01-01', TRUE)
ON CONFLICT (identificacion) DO NOTHING;

INSERT INTO usuarios (username, password, email, status, activo, intentos_fallidos, sesion_activa, id_persona)
SELECT 'Admin1234', crypt('Admin@1234', gen_salt('bf')), 'padmin@mail.com', 'ACTIVO', TRUE, 0, FALSE, p.id_persona
FROM persona p
WHERE p.identificacion = '0912345678'
ON CONFLICT (username) DO NOTHING;

INSERT INTO usuarios (username, password, email, status, activo, intentos_fallidos, sesion_activa, id_persona)
SELECT 'User1234', crypt('User@1234', gen_salt('bf')), 'puser@mail.com', 'ACTIVO', TRUE, 0, FALSE, p.id_persona
FROM persona p
WHERE p.identificacion = '0923456789'
ON CONFLICT (username) DO NOTHING;

INSERT INTO rol_usuario (id_usuario, id_rol, activo)
SELECT u.id_usuario, r.id_rol, TRUE
FROM usuarios u, rol r
WHERE u.username = 'Admin1234'
  AND r.nombre = 'ADMIN'
ON CONFLICT (id_usuario, id_rol) DO NOTHING;

INSERT INTO rol_usuario (id_usuario, id_rol, activo)
SELECT u.id_usuario, r.id_rol, TRUE
FROM usuarios u, rol r
WHERE u.username = 'User1234'
  AND r.nombre = 'USER'
ON CONFLICT (id_usuario, id_rol) DO NOTHING;

INSERT INTO rol_opciones (id_rol, id_opcion, activo)
SELECT r.id_rol, o.id_opcion, TRUE
FROM rol r, opciones o
WHERE r.nombre = 'ADMIN'
  AND o.nombre IN ('Dashboard','Usuarios','Personas','Roles','Opciones','Sesiones','Bienvenida','Mi Perfil')
ON CONFLICT (id_rol, id_opcion) DO NOTHING;

INSERT INTO rol_opciones (id_rol, id_opcion, activo)
SELECT r.id_rol, o.id_opcion, TRUE
FROM rol r, opciones o
WHERE r.nombre = 'USER'
  AND o.nombre IN ('Bienvenida','Mi Perfil')
ON CONFLICT (id_rol, id_opcion) DO NOTHING;

