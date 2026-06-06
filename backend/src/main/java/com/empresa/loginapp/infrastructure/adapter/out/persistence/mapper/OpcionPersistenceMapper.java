package com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper;

import com.empresa.loginapp.domain.model.Opcion;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.OpcionEntity;

public class OpcionPersistenceMapper {
    public Opcion toDomain(OpcionEntity e) {
        if (e == null) return null;
        return Opcion.builder().idOpcion(e.getIdOpcion()).nombre(e.getNombre()).ruta(e.getRuta())
                .icono(e.getIcono()).orden(e.getOrden()).activo(e.getActivo()).build();
    }

    public OpcionEntity toEntity(Opcion o) {
        if (o == null) return null;
        return OpcionEntity.builder().idOpcion(o.getIdOpcion()).nombre(o.getNombre()).ruta(o.getRuta())
                .icono(o.getIcono()).orden(o.getOrden()).activo(o.getActivo()).build();
    }
}
