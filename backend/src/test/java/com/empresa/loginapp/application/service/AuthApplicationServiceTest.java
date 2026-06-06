package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.BusinessException;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.infrastructure.security.JwtService;
import com.empresa.loginapp.shared.dto.request.LoginRequest;
import com.empresa.loginapp.shared.dto.response.MenuResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {
    @Mock
    UsuarioRepositoryPort usuarios;
    @Mock
    RolUsuarioRepositoryPort rolUsuarios;
    @Mock
    SesionRepositoryPort sesiones;
    @Mock
    MenuFunctionPort menuFunction;
    @Mock
    JwtService jwtService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    AuthApplicationService service;

    @BeforeEach
    void setup() {
        service = new AuthApplicationService(usuarios, rolUsuarios, sesiones, menuFunction, encoder, jwtService);
    }

    @Test
    void loginCorrectoConUsername() {
        Usuario usuario = usuario(encoder.encode("Admin@1234"));
        usuario.setIntentosFallidos(2);
        RolUsuario ru = RolUsuario.builder().usuario(usuario).rol(Rol.builder().idRol(1L).nombre("ADMIN").activo(true).build()).activo(true).build();
        when(usuarios.findByCredential("Admin1234")).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(ru));
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sesiones.save(any())).thenAnswer(i -> i.getArgument(0));
        when(menuFunction.menuPorRol(1L)).thenReturn(List.of(MenuResponse.builder().nombre("Dashboard").build()));
        when(jwtService.generateToken(any(), eq("ADMIN"))).thenReturn("token");
        LoginRequest request = login("Admin1234", "Admin@1234");
        assertThat(service.login(request).getToken()).isEqualTo("token");
        assertThat(usuario.getIntentosFallidos()).isZero();
        assertThat(usuario.getSesionActiva()).isTrue();
    }

    @Test
    void loginCorrectoConEmail() {
        Usuario usuario = usuario(encoder.encode("Admin@1234"));
        when(usuarios.findByCredential("admin@mail.com")).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().usuario(usuario).rol(Rol.builder().idRol(1L).nombre("ADMIN").build()).build()));
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sesiones.save(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(any(), any())).thenReturn("token");
        assertThat(service.login(login("admin@mail.com", "Admin@1234")).getToken()).isEqualTo("token");
    }

    @Test
    void passwordIncorrectoRegistraIntento() {
        Usuario usuario = usuario(encoder.encode("Admin@1234"));
        when(usuarios.findByCredential("Admin1234")).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().usuario(usuario).rol(Rol.builder().idRol(1L).nombre("ADMIN").build()).build()));
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThatThrownBy(() -> service.login(login("Admin1234", "Bad@1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Le quedan 2 intentos");
        assertThat(usuario.getIntentosFallidos()).isEqualTo(1);
    }

    @Test
    void bloqueoAlTercerIntento() {
        Usuario usuario = usuario(encoder.encode("Admin@1234"));
        usuario.setIntentosFallidos(2);
        when(usuarios.findByCredential("Admin1234")).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().usuario(usuario).rol(Rol.builder().idRol(1L).nombre("ADMIN").build()).build()));
        when(usuarios.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThatThrownBy(() -> service.login(login("Admin1234", "Bad@1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Usuario bloqueado por superar los 3 intentos permitidos");
        assertThat(usuario.getStatus()).isEqualTo("BLOQUEADO");
    }

    @Test
    void sesionActivaDuplicada() {
        Usuario usuario = usuario(encoder.encode("Admin@1234"));
        usuario.setSesionActiva(true);
        when(usuarios.findByCredential("Admin1234")).thenReturn(Optional.of(usuario));
        assertThatThrownBy(() -> service.login(login("Admin1234", "Admin@1234"))).isInstanceOf(BusinessException.class);
    }

    @Test
    void logoutCierraSesionActiva() {
        Usuario usuario = usuario(encoder.encode("Admin@1234"));
        usuario.setSesionActiva(true);
        Sesion sesion = Sesion.builder().idSesion(1L).idUsuario(1L).activa(true).mensaje("LOGIN_OK").build();
        when(usuarios.findByUsername("Admin1234")).thenReturn(Optional.of(usuario));
        when(sesiones.findActiveByUsuario(1L)).thenReturn(Optional.of(sesion));

        service.logout("Admin1234");

        assertThat(usuario.getSesionActiva()).isFalse();
        assertThat(sesion.getActiva()).isFalse();
        assertThat(sesion.getFechaCierre()).isNotNull();
        verify(usuarios).save(usuario);
        verify(sesiones).save(sesion);
    }

    private LoginRequest login(String credential, String password) {
        LoginRequest r = new LoginRequest(); r.setCredential(credential); r.setPassword(password); return r;
    }

    private Usuario usuario(String password) {
        return Usuario.builder().idUsuario(1L).username("Admin1234").email("admin@mail.com").password(password).status("ACTIVO").activo(true).sesionActiva(false).intentosFallidos(0).build();
    }
}
