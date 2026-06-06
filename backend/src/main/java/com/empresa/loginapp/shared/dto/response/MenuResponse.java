package com.empresa.loginapp.shared.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private Long idOpcion;
    private String nombre;
    private String ruta;
    private String icono;
    private Integer orden;
}
