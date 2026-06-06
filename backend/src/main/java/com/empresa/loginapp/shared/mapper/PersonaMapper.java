package com.empresa.loginapp.shared.mapper;

import com.empresa.loginapp.domain.model.Persona;
import com.empresa.loginapp.shared.dto.response.PersonaResponse;

public final class PersonaMapper {
    private PersonaMapper() {
    }

    public static PersonaResponse toResponse(Persona persona) {
        if (persona == null) return null;
        return PersonaResponse.builder()
                .idPersona(persona.getIdPersona())
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .identificacion(persona.getIdentificacion())
                .fechaNacimiento(persona.getFechaNacimiento())
                .activo(persona.getActivo())
                .build();
    }
}
