package com.empresa.loginapp.shared.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SimpleNameRequest {
    @NotBlank

    private String nombre;
}
