package com.empresa.loginapp.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolUsuario {
    private Long idRolUsuario;
    private Usuario usuario;
    private Rol rol;
    private Boolean activo;
}
