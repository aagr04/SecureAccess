package com.empresa.loginapp.shared.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumenResponse {
    private long totalUsuarios;
    private long usuariosActivos;
    private long usuariosInactivos;
    private long usuariosBloqueados;
    private long usuariosSesionActiva;
    private long usuariosSesionInactiva;
    private long totalSesionesFallidas;
}
