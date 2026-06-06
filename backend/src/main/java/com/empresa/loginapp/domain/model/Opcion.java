package com.empresa.loginapp.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {
    private Long idOpcion;
    private String nombre;
    private String ruta;
    private String icono;
    private Integer orden;
    private Boolean activo;
}
