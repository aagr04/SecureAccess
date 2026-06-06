package com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper;

import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;

public class UsuarioPersistenceMapper {
    private final PersonaPersistenceMapper personaMapper = new PersonaPersistenceMapper();
    public Usuario toDomain(UsuarioEntity e) {
        if (e == null) return null;
        return Usuario.builder().idUsuario(e.getIdUsuario()).username(e.getUsername()).password(e.getPassword())
                .email(e.getEmail()).status(e.getStatus()).activo(e.getActivo()).intentosFallidos(e.getIntentosFallidos())
                .sesionActiva(e.getSesionActiva()).persona(personaMapper.toDomain(e.getPersona()))
                .fechaCreacion(e.getFechaCreacion()).fechaActualizacion(e.getFechaActualizacion()).build();
    }

    public UsuarioEntity toEntity(Usuario u) {
        if (u == null) return null;
        return UsuarioEntity.builder().idUsuario(u.getIdUsuario()).username(u.getUsername()).password(u.getPassword())
                .email(u.getEmail()).status(u.getStatus()).activo(u.getActivo()).intentosFallidos(u.getIntentosFallidos())
                .sesionActiva(u.getSesionActiva()).persona(personaMapper.toEntity(u.getPersona()))
                .fechaCreacion(u.getFechaCreacion()).fechaActualizacion(u.getFechaActualizacion()).build();
    }
}
