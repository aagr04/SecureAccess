package com.empresa.loginapp.shared.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolOpcionRequest {
    @NotNull
    private Long idRol;
    @NotNull
    private Long idOpcion;
}
