package com.empresa.loginapp.domain.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    private Long idPersona;
    private String nombres;
    private String apellidos;
    private String identificacion;
    private LocalDate fechaNacimiento;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
