# Autenticacion con Secure Cookies y Redis

## Resumen

El sistema utiliza autenticacion mediante JWT almacenado en una cookie segura llamada `ACCESS_TOKEN`.
El frontend no guarda el token en `localStorage` ni `sessionStorage`.
El backend valida la cookie en cada peticion protegida y consulta Redis para verificar si el token fue invalidado por logout.

## Flujo de login

1. El usuario envia credenciales.
2. El backend valida usuario y contrasena.
3. El backend genera un JWT con `jti`.
4. El backend envia `Set-Cookie` con `ACCESS_TOKEN`.
5. El navegador guarda la cookie.
6. El frontend no lee el token.

## Flujo de consumo de API

1. React llama al backend con `withCredentials: true`.
2. El navegador adjunta automaticamente la cookie.
3. El backend lee `ACCESS_TOKEN`.
4. El backend valida firma, expiracion y `jti`.
5. El backend consulta Redis.
6. Si el token es valido, permite el acceso.
7. Si el token esta invalidado, responde `401`.

## Flujo de logout

1. El usuario hace logout.
2. El frontend llama al endpoint de logout.
3. El backend lee la cookie.
4. El backend extrae el `jti`.
5. El backend guarda el `jti` en Redis como `blacklist:token:{jti}`.
6. El backend expira la cookie.
7. Las demas pestanas pierden la sesion al intentar consumir APIs protegidas.

## Dos pestanas

Las pestanas del mismo navegador comparten la misma cookie.
Por eso, abrir varias pestanas no representa multiples sesiones independientes.
Si se cierra sesion en una pestana, el token se invalida en Redis y las otras pestanas quedan sin acceso.

## Otro navegador o dispositivo

Otro navegador o dispositivo genera otra cookie.
Un nuevo login real puede cerrar una sesion activa anterior en la base de datos para evitar bloqueos por sesiones huerfanas.
La restauracion de sesion en una pestana existente debe hacerse con `/api/auth/me`, no ejecutando login nuevamente.

## Diferencia entre 401 y 403

`401 Unauthorized` significa que el usuario no esta autenticado o su sesion expiro, fue invalidada en Redis o no tiene cookie valida.
Ante `401`, el frontend limpia el estado local y redirige al login.

`403 Forbidden` significa que el usuario si esta autenticado, pero no tiene permiso para acceder a un recurso.
Ante `403`, el frontend no debe cerrar sesion; debe mostrar acceso denegado o el mensaje de autorizacion correspondiente.

## Configuracion por ambiente

En desarrollo local puede usarse `app.security.cookie.secure=false` porque `localhost` normalmente trabaja con HTTP.

En produccion debe usarse `app.security.cookie.secure=true` porque la cookie debe viajar por HTTPS.

Si frontend y backend estan en dominios distintos en produccion, configurar:

```properties
app.security.cookie.same-site=None
app.security.cookie.secure=true
app.cors.allowed-origins=https://DOMINIO-REAL-DEL-FRONTEND
```

CORS debe permitir credenciales y no debe usar wildcard `*` con cookies:

```java
config.setAllowCredentials(true);
```

## Politica de sesiones

Las pestanas del mismo navegador comparten la misma cookie, por lo tanto se consideran una sola sesion logica.

No se bloquea la apertura de varias pestanas porque eso romperia la validacion solicitada de secure cookies.

Cuando el usuario hace logout en una pestana, el token se invalida en Redis y las demas pestanas pierden acceso.

Un nuevo login desde otro navegador o dispositivo puede considerarse una sesion distinta. En ese caso, el sistema puede cerrar una sesion activa anterior o aplicar una politica de control de sesiones definida por el negocio.
