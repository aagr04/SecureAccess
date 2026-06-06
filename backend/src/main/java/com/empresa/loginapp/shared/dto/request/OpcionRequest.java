package com.empresa.loginapp.shared.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OpcionRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String ruta;
    private String icono;
    private Integer orden;
}
