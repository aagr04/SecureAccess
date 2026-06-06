package com.empresa.loginapp.shared.mapper;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.shared.dto.response.SesionResponse;

public final class SesionMapper {
    private SesionMapper() {
    }

    public static SesionResponse toResponse(Sesion sesion) {
        if (sesion == null) return null;
        return SesionResponse.builder()
                .idSesion(sesion.getIdSesion())
                .idUsuario(sesion.getIdUsuario())
                .fechaIngreso(sesion.getFechaIngreso())
                .fechaCierre(sesion.getFechaCierre())
                .activa(sesion.getActiva())
                .exitoso(sesion.getExitoso())
                .mensaje(sesion.getMensaje())
                .intentosFallidos(sesion.getIntentosFallidos())
                .build();
    }
}
