package com.empresa.loginapp.shared.dto.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponse {
    private Long idPersona;
    private String nombres;
    private String apellidos;
    private String identificacion;
    private LocalDate fechaNacimiento;
    private Boolean activo;
}
