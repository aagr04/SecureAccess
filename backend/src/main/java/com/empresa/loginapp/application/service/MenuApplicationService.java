package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.*;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.in.MenuUseCase;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.shared.dto.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuApplicationService implements MenuUseCase {
    private final UsuarioRepositoryPort usuarios;
    private final RolUsuarioRepositoryPort rolUsuarios;
    private final MenuFunctionPort menuFunction;

    public List<MenuResponse> menuByAuthenticatedUser(String username) {
        Usuario usuario = usuarios.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        RolUsuario rolUsuario = rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario()).orElseThrow(() -> new BusinessException("Usuario sin rol activo"));
        return menuFunction.menuPorRol(rolUsuario.getRol().getIdRol());
    }
}
