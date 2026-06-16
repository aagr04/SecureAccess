package com.empresa.loginapp.shared.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private String token;
    private Long idUsuario;
    private String username;
    private String email;
    private String rol;
    private List<MenuResponse> menu;

    public AuthResponse withoutToken() {
        this.token = null;
        return this;
    }
}
