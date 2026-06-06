package com.empresa.loginapp.shared.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioRequest {
    @NotBlank
    private String username;
    private String password;
    private String email;
    private String status;
    private Long idPersona;
    private Long idRol;
    private String rol;
    private String nombres;
    private String apellidos;
    private String identificacion;
    private LocalDate fechaNacimiento;
}
