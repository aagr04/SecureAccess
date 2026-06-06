package com.empresa.loginapp.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolOpcion {
    private Long idRolOpcion;
    private Rol rol;
    private Opcion opcion;
    private Boolean activo;
}
