package com.empresa.loginapp.shared.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolOpcionResponse {
    private Long idRolOpcion;
    private RolResponse rol;
    private OpcionResponse opcion;
    private Boolean activo;
}
