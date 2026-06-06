-- Ejecutar conectado a loginapp_db.

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

