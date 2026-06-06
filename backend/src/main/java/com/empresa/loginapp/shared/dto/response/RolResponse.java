package com.empresa.loginapp.shared.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {
    private Long idRol;
    private String nombre;
    private Boolean activo;
}
