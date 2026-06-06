package com.empresa.loginapp.shared.mapper;

import com.empresa.loginapp.domain.model.Rol;
import com.empresa.loginapp.shared.dto.response.RolResponse;

public final class RolMapper {
    private RolMapper() {
    }

    public static RolResponse toResponse(Rol rol) {
        if (rol == null) return null;
        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .nombre(rol.getNombre())
                .activo(rol.getActivo())
                .build();
    }
}
