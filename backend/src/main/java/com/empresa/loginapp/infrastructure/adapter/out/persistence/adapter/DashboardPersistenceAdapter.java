package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.shared.dto.response.DashboardResumenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DashboardPersistenceAdapter implements DashboardRepositoryPort {
    private final UsuarioRepositoryPort usuarios;
    private final SesionRepositoryPort sesiones;
    public DashboardResumenResponse resumen() {
        return DashboardResumenResponse.builder()
                .totalUsuarios(usuarios.countAll())
                .usuariosActivos(usuarios.countByActivo(true))
                .usuariosInactivos(usuarios.countByActivo(false))
                .usuariosBloqueados(usuarios.countByStatus("BLOQUEADO"))
                .usuariosSesionActiva(usuarios.countBySesionActiva(true))
                .usuariosSesionInactiva(usuarios.countBySesionActiva(false))
                .totalSesionesFallidas(sesiones.countFailed())
                .build();
    }
}
