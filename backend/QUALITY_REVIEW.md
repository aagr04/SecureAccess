# Revision de Calidad Backend

## Resultado

El backend queda alineado con Arquitectura Hexagonal, Java 17, PostgreSQL, JWT, Maven Wrapper y estructura lista para abrir en IntelliJ IDEA desde la carpeta `backend`.

## Hallazgos corregidos

- El proyecto estaba configurado con Java 21; se cambio a Java 17 con `maven-compiler-plugin` usando `source`, `target` y `release` 17.
- Algunos controllers devolvian modelos de dominio, incluyendo `Usuario` con `password`; se agregaron DTOs de response y mappers para no exponer credenciales.
- Faltaban nombres de DTO requeridos por la prueba; se agregaron `CambiarEstadoUsuarioRequest`, `RecoverPasswordRequest`, `RolRequest`, `LoginResponse`, `UsuarioResponse`, `PersonaResponse`, `RolResponse`, `OpcionResponse`, `RolOpcionResponse`, `SesionResponse` y `DashboardResponse`.
- La seguridad devolvia 403 para una ruta protegida sin token; se agregaron handlers explicitos para 401 y 403.
- Se reemplazaron strings repetidos de estado por constantes en `UsuarioStatus`.
- Se agrego `BeanConfig` para registrar validadores y servicios puros de dominio como beans, manteniendo constructor injection.
- Se deshabilito `spring.jpa.open-in-view`.

## Validaciones de arquitectura

- `domain/model` no tiene Spring, JPA, `@Entity`, `@Table`, `@Autowired` ni `JpaRepository`.
- Los controllers consumen casos de uso.
- Los application services dependen de puertos de salida.
- Los adapters de persistencia implementan puertos.
- Criteria Builder esta en `infrastructure/adapter/out/persistence/criteria`.
- JWT esta en `infrastructure/security`.

## Seguridad

- Login por username o email.
- Password cifrado con BCrypt.
- JWT con expiracion y secret configurable.
- Rutas publicas limitadas a login y recover.
- Dashboard, sesiones, bulk y cambio de estado protegidos para ADMIN.
- Token invalido o ausente devuelve 401.
- Usuario autenticado sin permiso devuelve 403.
- Responses de usuario no exponen password.

## Nota de entorno

El proyecto esta configurado para compilar bytecode Java 17. En esta maquina `java -version` muestra Java 21, pero Maven compilo con `release 17`. Para cumplir estrictamente el entorno solicitado, instalar/configurar JDK 17 en `JAVA_HOME`.
