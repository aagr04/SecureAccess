-- Ejecutar conectado a loginapp_db.

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

