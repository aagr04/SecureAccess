package com.empresa.loginapp.shared.mapper;

import com.empresa.loginapp.domain.model.Opcion;
import com.empresa.loginapp.shared.dto.response.OpcionResponse;

public final class OpcionMapper {
    private OpcionMapper() {
    }

    public static OpcionResponse toResponse(Opcion opcion) {
        if (opcion == null) return null;
        return OpcionResponse.builder()
                .idOpcion(opcion.getIdOpcion())
                .nombre(opcion.getNombre())
                .ruta(opcion.getRuta())
                .icono(opcion.getIcono())
                .orden(opcion.getOrden())
                .activo(opcion.getActivo())
                .build();
    }
}
