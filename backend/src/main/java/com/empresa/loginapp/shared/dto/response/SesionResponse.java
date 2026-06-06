package com.empresa.loginapp.shared.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SesionResponse {
    private Long idSesion;
    private Long idUsuario;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaCierre;
    private Boolean activa;
    private Boolean exitoso;
    private String mensaje;
    private Integer intentosFallidos;
}
