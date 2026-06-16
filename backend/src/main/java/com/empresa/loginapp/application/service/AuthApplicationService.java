package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.*;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.in.AuthUseCase;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.infrastructure.security.JwtService;
import com.empresa.loginapp.infrastructure.security.TokenSessionService;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthApplicationService implements AuthUseCase {
    private final UsuarioRepositoryPort usuarios;
    private final RolUsuarioRepositoryPort rolUsuarios;
    private final SesionRepositoryPort sesiones;
    private final MenuFunctionPort menuFunction;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenSessionService tokenSessionService;

    @Autowired
    public AuthApplicationService(
            UsuarioRepositoryPort usuarios,
            RolUsuarioRepositoryPort rolUsuarios,
            SesionRepositoryPort sesiones,
            MenuFunctionPort menuFunction,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TokenSessionService tokenSessionService) {
        this.usuarios = usuarios;
        this.rolUsuarios = rolUsuarios;
        this.sesiones = sesiones;
        this.menuFunction = menuFunction;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenSessionService = tokenSessionService;
    }

    public AuthApplicationService(
            UsuarioRepositoryPort usuarios,
            RolUsuarioRepositoryPort rolUsuarios,
            SesionRepositoryPort sesiones,
            MenuFunctionPort menuFunction,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this(usuarios, rolUsuarios, sesiones, menuFunction, passwordEncoder, jwtService, null);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarios.findByCredential(request.getCredential()).orElseThrow(() -> new BusinessException("Credenciales invalidas"));
        if (!Boolean.TRUE.equals(usuario.getActivo())) throw new BusinessException("Usuario inactivo");
        if (UsuarioStatus.BLOQUEADO.equalsIgnoreCase(usuario.getStatus())) throw new BusinessException("Usuario bloqueado");
        RolUsuario rolUsuario = rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario()).orElseThrow(() -> new BusinessException("Usuario sin rol activo"));
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessException(failLogin(usuario));
        }
        closeActiveSession(usuario.getIdUsuario(), "LOGIN_REPLACED");
        usuario.setIntentosFallidos(0);
        usuario.setSesionActiva(true);
        usuario.setStatus(UsuarioStatus.ACTIVO);
        usuarios.save(usuario);
        sesiones.save(Sesion.builder().idUsuario(usuario.getIdUsuario()).fechaIngreso(LocalDateTime.now()).activa(true).exitoso(true).mensaje("LOGIN_OK").intentosFallidos(0).build());
        List<MenuResponse> menu = menuFunction.menuPorRol(rolUsuario.getRol().getIdRol());
        String token = jwtService.generateToken(usuario, rolUsuario.getRol().getNombre());
        return AuthResponse.builder().token(token).idUsuario(usuario.getIdUsuario()).username(usuario.getUsername()).email(usuario.getEmail()).rol(rolUsuario.getRol().getNombre()).menu(menu).build();
    }

    @Transactional
    public AuthResponse me(String username) {
        Usuario usuario = usuarios.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        RolUsuario rolUsuario = rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario()).orElseThrow(() -> new BusinessException("Usuario sin rol activo"));
        List<MenuResponse> menu = menuFunction.menuPorRol(rolUsuario.getRol().getIdRol());
        return AuthResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(rolUsuario.getRol().getNombre())
                .menu(menu)
                .build();
    }

    @Transactional
    public void logout(String username) {
        logout(username, null);
    }

    @Transactional
    public void logout(String username, String token) {
        if (tokenSessionService != null && token != null && !token.isBlank()) {
            tokenSessionService.invalidate(jwtService.extractJti(token), jwtService.remainingTtl(token));
        }
        if (username == null || username.isBlank()) {
            return;
        }
        Usuario usuario = usuarios.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        usuario.setSesionActiva(false);
        usuarios.save(usuario);
        sesiones.findActiveByUsuario(usuario.getIdUsuario()).ifPresent(s -> {
            s.setActiva(false);
            s.setFechaCierre(LocalDateTime.now());
            s.setMensaje("LOGOUT_OK");
            sesiones.save(s);
        });
    }

    public String recover(RecoverRequest request) {
        Usuario usuario = usuarios.findByCredential(request.getCredential()).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return "Solicitud de recuperacion registrada para " + usuario.getEmail();
    }

    private void closeActiveSession(Long idUsuario, String message) {
        sesiones.findActiveByUsuario(idUsuario).ifPresent(s -> {
            s.setActiva(false);
            s.setFechaCierre(LocalDateTime.now());
            s.setMensaje(message);
            sesiones.save(s);
        });
    }

    private String failLogin(Usuario usuario) {
        int failed = usuario.getIntentosFallidos() == null ? 1 : usuario.getIntentosFallidos() + 1;
        usuario.setIntentosFallidos(failed);
        String message;
        if (failed >= 3) {
            usuario.setStatus(UsuarioStatus.BLOQUEADO);
            message = "Usuario bloqueado por superar los 3 intentos permitidos";
        } else {
            int remaining = 3 - failed;
            message = remaining == 1 ? "Contrasena incorrecta. Le queda 1 intento" : "Contrasena incorrecta. Le quedan " + remaining + " intentos";
        }
        usuarios.save(usuario);
        sesiones.save(Sesion.builder().idUsuario(usuario.getIdUsuario()).fechaIngreso(LocalDateTime.now()).activa(false).exitoso(false).mensaje(message).intentosFallidos(failed).build());
        return message;
    }
}
