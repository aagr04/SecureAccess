package com.empresa.loginapp.infrastructure.security;

import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepositoryPort usuarios;
    private final RolUsuarioRepositoryPort rolUsuarios;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarios.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        RolUsuario rolUsuario = rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario()).orElse(null);
        String role = rolUsuario == null ? "USER" : rolUsuario.getRol().getNombre();
        return new User(usuario.getUsername(), usuario.getPassword(), Boolean.TRUE.equals(usuario.getActivo()), true, true,
                !UsuarioStatus.BLOQUEADO.equalsIgnoreCase(usuario.getStatus()), List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }
}
