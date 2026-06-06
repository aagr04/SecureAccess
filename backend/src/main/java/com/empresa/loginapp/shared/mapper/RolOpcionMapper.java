package com.empresa.loginapp.shared.mapper;

import com.empresa.loginapp.domain.model.RolOpcion;
import com.empresa.loginapp.shared.dto.response.RolOpcionResponse;

public final class RolOpcionMapper {
    private RolOpcionMapper() {
    }

    public static RolOpcionResponse toResponse(RolOpcion rolOpcion) {
        if (rolOpcion == null) return null;
        return RolOpcionResponse.builder()
                .idRolOpcion(rolOpcion.getIdRolOpcion())
                .rol(RolMapper.toResponse(rolOpcion.getRol()))
                .opcion(OpcionMapper.toResponse(rolOpcion.getOpcion()))
                .activo(rolOpcion.getActivo())
                .build();
    }
}
