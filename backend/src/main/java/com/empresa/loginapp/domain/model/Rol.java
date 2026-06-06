package com.empresa.loginapp.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    private Long idRol;
    private String nombre;
    private Boolean activo;
}
