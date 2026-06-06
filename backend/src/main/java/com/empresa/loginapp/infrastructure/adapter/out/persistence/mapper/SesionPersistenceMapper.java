package com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.*;

public class SesionPersistenceMapper {
    public Sesion toDomain(SesionEntity e) {
        if (e == null) return null;
        return Sesion.builder().idSesion(e.getIdSesion()).idUsuario(e.getUsuario().getIdUsuario())
                .fechaIngreso(e.getFechaIngreso()).fechaCierre(e.getFechaCierre()).activa(e.getActiva())
                .exitoso(e.getExitoso()).mensaje(e.getMensaje()).intentosFallidos(e.getIntentosFallidos()).build();
    }

    public SesionEntity toEntity(Sesion s, UsuarioEntity usuario) {
        if (s == null) return null;
        return SesionEntity.builder().idSesion(s.getIdSesion()).usuario(usuario).fechaIngreso(s.getFechaIngreso())
                .fechaCierre(s.getFechaCierre()).activa(s.getActiva()).exitoso(s.getExitoso())
                .mensaje(s.getMensaje()).intentosFallidos(s.getIntentosFallidos()).build();
    }
}
