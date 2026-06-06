package com.empresa.loginapp.shared.dto.request;

import lombok.Data;

@Data
public class UsuarioFilterRequest {
    private String nombres;
    private String apellidos;
    private String identificacion;
    private String username;
    private String email;
    private String status;
    private String rol;
}
