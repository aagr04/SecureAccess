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
    FROM opciones o
    INNER JOIN rol_opciones ro ON ro.id_opcion = o.id_opcion
    WHERE ro.id_rol = p_id_rol
      AND ro.activo = TRUE
      AND o.activo = TRUE
    ORDER BY o.orden NULLS LAST, o.nombre;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION fn_intentos_fallidos_usuario(p_id_usuario BIGINT)
RETURNS INTEGER AS $$
DECLARE
    v_intentos INTEGER;
BEGIN
    SELECT COALESCE(MAX(s.intentos_fallidos), 0)
    INTO v_intentos
    FROM sesiones s
    WHERE s.id_usuario = p_id_usuario;

    RETURN v_intentos;
END;
$$ LANGUAGE plpgsql STABLE;
