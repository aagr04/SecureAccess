package com.empresa.loginapp.shared.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long idUsuario;
    private String username;
    private String email;
    private String status;
    private Boolean activo;
    private Integer intentosFallidos;
    private Boolean sesionActiva;
    private PersonaResponse persona;
}
