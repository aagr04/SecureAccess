create database loginapp_db;

SELECT * FROM pg_catalog.pg_database;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS persona (
    id_persona BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    identificacion VARCHAR(10) UNIQUE NOT NULL,
    fecha_nacimiento DATE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    intentos_fallidos INTEGER DEFAULT 0,
    sesion_activa BOOLEAN DEFAULT FALSE,
    id_persona BIGINT UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    CONSTRAINT fk_usuarios_persona FOREIGN KEY (id_persona) REFERENCES persona(id_persona)
);

CREATE TABLE IF NOT EXISTS rol (
    id_rol BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS rol_usuario (
    id_rol_usuario BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_rol_usuario_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_rol_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    CONSTRAINT uk_rol_usuario UNIQUE (id_usuario, id_rol)
);

CREATE TABLE IF NOT EXISTS opciones (
    id_opcion BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruta VARCHAR(150) NOT NULL,
    icono VARCHAR(50),
    orden INTEGER,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT uk_opciones_nombre_ruta UNIQUE (nombre, ruta)
);

CREATE TABLE IF NOT EXISTS rol_opciones (
    id_rol_opcion BIGSERIAL PRIMARY KEY,
    id_rol BIGINT NOT NULL,
    id_opcion BIGINT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_rol_opciones_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    CONSTRAINT fk_rol_opciones_opcion FOREIGN KEY (id_opcion) REFERENCES opciones(id_opcion),
    CONSTRAINT uk_rol_opciones UNIQUE (id_rol, id_opcion)
);

CREATE TABLE IF NOT EXISTS sesiones (
    id_sesion BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    fecha_ingreso TIMESTAMP,
    fecha_cierre TIMESTAMP,
    activa BOOLEAN DEFAULT FALSE,
    exitoso BOOLEAN DEFAULT FALSE,
    mensaje VARCHAR(255),
    intentos_fallidos INTEGER DEFAULT 0,
    CONSTRAINT fk_sesiones_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuarios(username);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON persona(identificacion);
CREATE INDEX IF NOT EXISTS idx_usuarios_status ON usuarios(status);
CREATE INDEX IF NOT EXISTS idx_usuarios_activo ON usuarios(activo);
CREATE INDEX IF NOT EXISTS idx_sesiones_id_usuario ON sesiones(id_usuario);
CREATE INDEX IF NOT EXISTS idx_rol_usuario_id_usuario ON rol_usuario(id_usuario);
CREATE INDEX IF NOT EXISTS idx_rol_usuario_id_rol ON rol_usuario(id_rol);
CREATE INDEX IF NOT EXISTS idx_rol_opciones_id_rol ON rol_opciones(id_rol);
CREATE INDEX IF NOT EXISTS idx_rol_opciones_id_opcion ON rol_opciones(id_opcion);


CREATE OR REPLACE FUNCTION fn_menu_por_rol(p_id_rol BIGINT)
RETURNS TABLE (
    id_opcion BIGINT,
    nombre VARCHAR,
    ruta VARCHAR,
    icono VARCHAR,
    orden INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT o.id_opcion, o.nombre, o.ruta, o.icono, o.orden
    FROM rol r
    JOIN rol_opciones ro ON ro.id_rol = r.id_rol
    JOIN opciones o ON o.id_opcion = ro.id_opcion
    WHERE r.id_rol = p_id_rol
      AND r.activo = TRUE
      AND ro.activo = TRUE
      AND o.activo = TRUE
    ORDER BY o.orden NULLS LAST, o.nombre;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION fn_intentos_fallidos_usuario(p_id_usuario BIGINT)
RETURNS INTEGER AS $$
DECLARE
    total INTEGER;
BEGIN
    SELECT COUNT(*) INTO total
    FROM sesiones
    WHERE id_usuario = p_id_usuario
      AND exitoso = FALSE;
    RETURN COALESCE(total, 0);
END;
$$ LANGUAGE plpgsql;


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
FROM persona p WHERE p.identificacion = '0912345678'
ON CONFLICT (username) DO NOTHING;

INSERT INTO usuarios (username, password, email, status, activo, intentos_fallidos, sesion_activa, id_persona)
SELECT 'User1234', crypt('User@1234', gen_salt('bf')), 'puser@mail.com', 'ACTIVO', TRUE, 0, FALSE, p.id_persona
FROM persona p WHERE p.identificacion = '0923456789'
ON CONFLICT (username) DO NOTHING;

INSERT INTO rol_usuario (id_usuario, id_rol, activo)
SELECT u.id_usuario, r.id_rol, TRUE FROM usuarios u, rol r
WHERE u.username = 'Admin1234' AND r.nombre = 'ADMIN'
ON CONFLICT (id_usuario, id_rol) DO NOTHING;

INSERT INTO rol_usuario (id_usuario, id_rol, activo)
SELECT u.id_usuario, r.id_rol, TRUE FROM usuarios u, rol r
WHERE u.username = 'User1234' AND r.nombre = 'USER'
ON CONFLICT (id_usuario, id_rol) DO NOTHING;

INSERT INTO rol_opciones (id_rol, id_opcion, activo)
SELECT r.id_rol, o.id_opcion, TRUE FROM rol r, opciones o
WHERE r.nombre = 'ADMIN' AND o.nombre IN ('Dashboard','Usuarios','Personas','Roles','Opciones','Sesiones','Bienvenida','Mi Perfil')
ON CONFLICT (id_rol, id_opcion) DO NOTHING;

INSERT INTO rol_opciones (id_rol, id_opcion, activo)
SELECT r.id_rol, o.id_opcion, TRUE FROM rol r, opciones o
WHERE r.nombre = 'USER' AND o.nombre IN ('Bienvenida','Mi Perfil')
ON CONFLICT (id_rol, id_opcion) DO NOTHING;



SELECT * FROM usuarios ORDER BY id_usuario;
SELECT * FROM rol ORDER BY id_rol;

SELECT * FROM fn_menu_por_rol((SELECT id_rol FROM rol WHERE nombre = 'ADMIN'));
SELECT * FROM fn_menu_por_rol((SELECT id_rol FROM rol WHERE nombre = 'USER'));

SELECT * FROM sesiones ORDER BY id_sesion DESC;
SELECT * FROM usuarios WHERE activo = TRUE ORDER BY id_usuario;
SELECT * FROM usuarios WHERE status = 'BLOQUEADO' ORDER BY id_usuario;

SELECT fn_intentos_fallidos_usuario((SELECT id_usuario FROM usuarios WHERE username = 'Admin1234'));

SELECT u.username, u.email, r.nombre AS rol
FROM usuarios u
JOIN rol_usuario ru ON ru.id_usuario = u.id_usuario AND ru.activo = TRUE
JOIN rol r ON r.id_rol = ru.id_rol
WHERE u.activo = TRUE;
