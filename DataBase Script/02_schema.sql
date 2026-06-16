-- Ejecutar conectado a loginapp_db.

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
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
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

CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON persona(identificacion);
CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuarios(username);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuarios_status ON usuarios(status);
CREATE INDEX IF NOT EXISTS idx_usuarios_activo ON usuarios(activo);
CREATE INDEX IF NOT EXISTS idx_sesiones_id_usuario ON sesiones(id_usuario);
CREATE INDEX IF NOT EXISTS idx_sesiones_activa ON sesiones(activa);
CREATE INDEX IF NOT EXISTS idx_rol_usuario_id_usuario ON rol_usuario(id_usuario);
CREATE INDEX IF NOT EXISTS idx_rol_usuario_id_rol ON rol_usuario(id_rol);
CREATE INDEX IF NOT EXISTS idx_rol_opciones_id_rol ON rol_opciones(id_rol);
CREATE INDEX IF NOT EXISTS idx_rol_opciones_id_opcion ON rol_opciones(id_opcion);
