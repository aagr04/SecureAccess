package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.*;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.in.AuthUseCase;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.infrastructure.security.JwtService;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthApplicationService implements AuthUseCase {
    private final UsuarioRepositoryPort usuarios;
    private final RolUsuarioRepositoryPort rolUsuarios;
    private final SesionRepositoryPort sesiones;
    private final MenuFunctionPort menuFunction;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarios.findByCredential(request.getCredential()).orElseThrow(() -> new BusinessException("Credenciales invalidas"));
        if (!Boolean.TRUE.equals(usuario.getActivo())) throw new BusinessException("Usuario inactivo");
        if (UsuarioStatus.BLOQUEADO.equalsIgnoreCase(usuario.getStatus())) throw new BusinessException("Usuario bloqueado");
        if (sesiones.findActiveByUsuario(usuario.getIdUsuario()).isPresent()) throw new BusinessException("El usuario ya tiene una sesion activa");
        RolUsuario rolUsuario = rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario()).orElseThrow(() -> new BusinessException("Usuario sin rol activo"));
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessException(failLogin(usuario));
        }
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
    public void logout(String username) {
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
