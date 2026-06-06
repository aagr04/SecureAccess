package com.empresa.loginapp.shared.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoUsuarioRequest {
    @NotBlank
    private String status;
    private Boolean activo;
}
