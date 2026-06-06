package com.empresa.loginapp.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {
    private Long idSesion;
    private Long idUsuario;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaCierre;
    private Boolean activa;
    private Boolean exitoso;
    private String mensaje;
    private Integer intentosFallidos;
}
