package com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper;

import com.empresa.loginapp.domain.model.Rol;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolEntity;

public class RolPersistenceMapper {
    public Rol toDomain(RolEntity e) {
        if (e == null) return null;
        return Rol.builder().idRol(e.getIdRol()).nombre(e.getNombre()).activo(e.getActivo()).build();
    }

    public RolEntity toEntity(Rol r) {
        if (r == null) return null;
        return RolEntity.builder().idRol(r.getIdRol()).nombre(r.getNombre()).activo(r.getActivo()).build();
    }
}
