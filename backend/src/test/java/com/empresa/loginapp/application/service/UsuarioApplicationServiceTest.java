package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.BusinessException;
import com.empresa.loginapp.domain.exception.ForbiddenException;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.domain.service.*;
import com.empresa.loginapp.shared.dto.request.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationServiceTest {
    @Mock
    UsuarioRepositoryPort usuarios;
    @Mock
    PersonaRepositoryPort personas;
    @Mock
    RolRepositoryPort roles;
    @Mock
    RolUsuarioRepositoryPort rolUsuarios;
    UsuarioApplicationService service;

    @BeforeEach
    void setup() {
        service = new UsuarioApplicationService(usuarios, personas, roles, rolUsuarios, new BCryptPasswordEncoder(),
                new UsernameValidator(), new PasswordValidator(), new IdentificacionValidator(), new EmailGeneratorService(), new UsuarioPermissionPolicy());
    }

    @Test
    void crearUsuarioValidoYCuentaUnica() {
        UsuarioRequest r = request();
        when(usuarios.findAllEmails()).thenReturn(Set.of());
        when(personas.findByIdentificacion("0912345678")).thenReturn(Optional.empty());
        when(personas.save(any())).thenAnswer(i -> Persona.builder().idPersona(1L).nombres(r.getNombres()).apellidos(r.getApellidos()).identificacion(r.getIdentificacion()).activo(true).build());
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.create(r).getEmail()).isEqualTo("padmin@mail.com");
        when(usuarios.existsActiveByPersonaIdentificacion("0912345678")).thenReturn(true);
        assertThatThrownBy(() -> service.create(r)).isInstanceOf(BusinessException.class);
    }

    @Test
    void usernamePasswordIdentificacionInvalidos() {
        UsuarioRequest usernameInvalido = request(); usernameInvalido.setUsername("admin@123");
        assertThatThrownBy(() -> service.create(usernameInvalido)).isInstanceOf(BusinessException.class);
        UsuarioRequest passwordInvalido = request(); passwordInvalido.setPassword("Admin 1234");
        assertThatThrownBy(() -> service.create(passwordInvalido)).isInstanceOf(BusinessException.class);
        UsuarioRequest identificacionInvalida = request(); identificacionInvalida.setIdentificacion("0911115678");
        assertThatThrownBy(() -> service.create(identificacionInvalida)).isInstanceOf(BusinessException.class);
    }

    @Test
    void emailDuplicadoConNumero() {
        UsuarioRequest r = request();
        when(usuarios.findAllEmails()).thenReturn(Set.of("padmin@mail.com"));
        when(personas.findByIdentificacion(any())).thenReturn(Optional.empty());
        when(personas.save(any())).thenAnswer(i -> Persona.builder().idPersona(1L).nombres(r.getNombres()).apellidos(r.getApellidos()).identificacion(r.getIdentificacion()).activo(true).build());
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.create(r).getEmail()).isEqualTo("padmin1@mail.com");
    }

    @Test
    void ignoraEmailManualDelRequest() {
        UsuarioRequest r = request();
        r.setEmail("manual@mail.com");
        when(usuarios.findAllEmails()).thenReturn(Set.of());
        when(personas.findByIdentificacion(any())).thenReturn(Optional.empty());
        when(personas.save(any())).thenAnswer(i -> Persona.builder().idPersona(1L).nombres(r.getNombres()).apellidos(r.getApellidos()).identificacion(r.getIdentificacion()).activo(true).build());
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));

        assertThat(service.create(r).getEmail()).isEqualTo("padmin@mail.com");
        verify(usuarios, never()).existsByEmail("manual@mail.com");
    }

    @Test
    void softDeleteMarcaUsuarioInactivoYSinSesion() {
        Usuario admin = Usuario.builder().idUsuario(10L).username("Admin1234").activo(true).build();
        Persona persona = Persona.builder().idPersona(2L).activo(true).build();
        Usuario usuario = Usuario.builder().idUsuario(1L).username("User1234").activo(true).status(UsuarioStatus.ACTIVO).sesionActiva(true).persona(persona).build();
        when(usuarios.findByUsername("Admin1234")).thenReturn(Optional.of(admin));
        when(usuarios.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(10L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("ADMIN").build()).build()));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("USER").build()).build()));
        when(usuarios.existsAnotherActiveByPersonaId(2L, 1L)).thenReturn(false);
        service.delete("Admin1234", 1L);
        assertThat(usuario.getActivo()).isFalse();
        assertThat(usuario.getStatus()).isEqualTo(UsuarioStatus.INACTIVO);
        assertThat(usuario.getSesionActiva()).isFalse();
        assertThat(persona.getActivo()).isFalse();
        verify(usuarios).save(usuario);
        verify(personas).save(persona);
    }

    @Test
    void actualizarSinPasswordMantienePasswordActual() {
        Usuario admin = Usuario.builder().idUsuario(10L).username("Admin1234").activo(true).build();
        Persona persona = Persona.builder().idPersona(1L).nombres("Persona").apellidos("Admin").identificacion("0912345678").activo(true).build();
        Usuario usuario = Usuario.builder().idUsuario(1L).username("User1234").password("hash-actual").status("ACTIVO").activo(true).persona(persona).build();
        UsuarioRequest r = request();
        r.setUsername("User1234");
        r.setPassword(null);
        when(usuarios.findByUsername("Admin1234")).thenReturn(Optional.of(admin));
        when(usuarios.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(10L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("ADMIN").build()).build()));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("USER").build()).build()));
        when(personas.save(persona)).thenReturn(persona);
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario actualizado = service.update("Admin1234", 1L, r);

        assertThat(actualizado.getPassword()).isEqualTo("hash-actual");
        verify(usuarios).save(usuario);
    }

    @Test
    void noActualizaAdministradoresDesdeMantenimiento() {
        Usuario admin = Usuario.builder().idUsuario(10L).username("Admin1234").activo(true).build();
        Usuario usuario = Usuario.builder().idUsuario(1L).username("Admin5678").password("hash-actual").status("ACTIVO").activo(true).build();
        when(usuarios.findByUsername("Admin1234")).thenReturn(Optional.of(admin));
        when(usuarios.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(10L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("ADMIN").build()).build()));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("ADMIN").build()).build()));

        assertThatThrownBy(() -> service.update("Admin1234", 1L, request())).isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("administrador");
    }

    @Test
    void userSoloActualizaSusDatos() {
        Persona persona = Persona.builder().idPersona(1L).nombres("Persona").apellidos("Admin").identificacion("0912345678").activo(true).build();
        Usuario user = Usuario.builder().idUsuario(1L).username("User1234").password("hash-actual").status("ACTIVO").activo(true).persona(persona).build();
        Usuario other = Usuario.builder().idUsuario(2L).username("Other1234").password("hash-actual").status("ACTIVO").activo(true).build();
        UsuarioRequest r = request();
        r.setUsername("User1234");
        r.setPassword(null);
        when(usuarios.findByUsername("User1234")).thenReturn(Optional.of(user));
        when(usuarios.findById(1L)).thenReturn(Optional.of(user));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("USER").build()).build()));
        when(personas.save(persona)).thenReturn(persona);
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));

        assertThat(service.update("User1234", 1L, r).getUsername()).isEqualTo("User1234");

        when(usuarios.findById(2L)).thenReturn(Optional.of(other));
        when(rolUsuarios.findActiveByUsuarioId(2L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("USER").build()).build()));
        assertThatThrownBy(() -> service.update("User1234", 2L, r)).isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("propios datos");
    }

    @Test
    void noDesactivaPersonaSiTieneOtroUsuarioActivo() {
        Usuario admin = Usuario.builder().idUsuario(10L).username("Admin1234").activo(true).build();
        Persona persona = Persona.builder().idPersona(2L).activo(true).build();
        Usuario usuario = Usuario.builder().idUsuario(1L).username("User1234").activo(true).status(UsuarioStatus.ACTIVO).sesionActiva(true).persona(persona).build();
        when(usuarios.findByUsername("Admin1234")).thenReturn(Optional.of(admin));
        when(usuarios.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(10L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("ADMIN").build()).build()));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().rol(Rol.builder().nombre("USER").build()).build()));
        when(usuarios.existsAnotherActiveByPersonaId(2L, 1L)).thenReturn(true);

        service.delete("Admin1234", 1L);

        assertThat(persona.getActivo()).isTrue();
        verify(personas, never()).save(any());
    }

    @Test
    void cargaMasivaCsvUsaColumnasOficialesYGeneraEmail() {
        String csv = "nombres,apellidos,identificacion,fechaNacimiento,username,password,rol\n" +
                "Juan Alberto,Piguave Loor,1203574901,1990-01-01,Juan2024A,Clave@123,USER\n";
        when(usuarios.findAllEmails()).thenReturn(Set.of());
        when(personas.findByIdentificacion("1203574901")).thenReturn(Optional.empty());
        when(personas.save(any())).thenAnswer(i -> {
            Persona p = i.getArgument(0);
            p.setIdPersona(1L);
            return p;
        });
        when(roles.findByNombre("USER")).thenReturn(Optional.of(Rol.builder().idRol(2L).nombre("USER").build()));
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));

        assertThat(service.bulkUpload(BulkUploadRequest.builder().filename("usuarios.csv").content(csv.getBytes()).build()).getExitosos()).isEqualTo(1);
        verify(usuarios).save(argThat(u -> "Juan2024A".equals(u.getUsername()) && "jpiguavel@mail.com".equals(u.getEmail())));
    }

    @Test
    void cargaMasivaRechazaArchivoVacioYExtensionInvalida() {
        assertThatThrownBy(() -> service.bulkUpload(BulkUploadRequest.builder().filename("usuarios.csv").content(new byte[0]).build()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Archivo vacio");
        assertThatThrownBy(() -> service.bulkUpload(BulkUploadRequest.builder().filename("usuarios.txt").content("x".getBytes()).build()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Extension invalida");
    }

    private UsuarioRequest request() {
        UsuarioRequest r = new UsuarioRequest();
        r.setUsername("Admin1234"); r.setPassword("AdminViamatica@500"); r.setNombres("Persona"); r.setApellidos("Admin"); r.setIdentificacion("0912345678");
        return r;
    }
}
