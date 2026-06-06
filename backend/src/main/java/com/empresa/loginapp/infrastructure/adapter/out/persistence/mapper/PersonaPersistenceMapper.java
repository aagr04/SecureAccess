package com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper;

import com.empresa.loginapp.domain.model.Persona;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.PersonaEntity;

public class PersonaPersistenceMapper {
    public Persona toDomain(PersonaEntity e) {
        if (e == null) return null;
        return Persona.builder().idPersona(e.getIdPersona()).nombres(e.getNombres()).apellidos(e.getApellidos())
                .identificacion(e.getIdentificacion()).fechaNacimiento(e.getFechaNacimiento()).activo(e.getActivo())
                .fechaCreacion(e.getFechaCreacion()).fechaActualizacion(e.getFechaActualizacion()).build();
    }

    public PersonaEntity toEntity(Persona p) {
        if (p == null) return null;
        return PersonaEntity.builder().idPersona(p.getIdPersona()).nombres(p.getNombres()).apellidos(p.getApellidos())
                .identificacion(p.getIdentificacion()).fechaNacimiento(p.getFechaNacimiento()).activo(p.getActivo())
                .fechaCreacion(p.getFechaCreacion()).fechaActualizacion(p.getFechaActualizacion()).build();
    }
}
