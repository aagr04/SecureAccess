package com.empresa.loginapp.shared.mapper;

import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.shared.dto.response.UsuarioResponse;

public final class UsuarioMapper {
    private UsuarioMapper() {
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .status(usuario.getStatus())
                .activo(usuario.getActivo())
                .intentosFallidos(usuario.getIntentosFallidos())
                .sesionActiva(usuario.getSesionActiva())
                .rol(usuario.getRol())
                .persona(PersonaMapper.toResponse(usuario.getPersona()))
                .build();
    }
}
