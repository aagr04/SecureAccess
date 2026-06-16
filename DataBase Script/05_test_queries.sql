-- Ejecutar conectado a loginapp_db.

SELECT * FROM rol;
SELECT * FROM opciones ORDER BY orden, nombre;
SELECT * FROM usuarios ORDER BY id_usuario;
SELECT * FROM sesiones ORDER BY id_sesion;

SELECT * FROM fn_menu_por_rol((SELECT id_rol FROM rol WHERE nombre = 'ADMIN'));
