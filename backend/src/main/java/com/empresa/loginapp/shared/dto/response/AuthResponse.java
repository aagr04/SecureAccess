package com.empresa.loginapp.shared.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long idUsuario;
    private String username;
    private String email;
    private String rol;
    private List<MenuResponse> menu;
}
