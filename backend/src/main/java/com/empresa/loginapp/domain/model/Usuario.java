package com.empresa.loginapp.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    private String username;
    private String password;
    private String email;
    private String status;
    private Boolean activo;
    private Integer intentosFallidos;
    private Boolean sesionActiva;
    private Persona persona;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
