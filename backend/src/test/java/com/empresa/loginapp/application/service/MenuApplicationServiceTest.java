package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.shared.dto.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuApplicationServiceTest {
    @Mock
    UsuarioRepositoryPort usuarios;
    @Mock
    RolUsuarioRepositoryPort rolUsuarios;
    @Mock
    MenuFunctionPort menuFunction;

    @Test
    void menuPorRolUsaFuncionDesdePuerto() {
        Usuario usuario = Usuario.builder().idUsuario(1L).username("Admin1234").build();
        Rol rol = Rol.builder().idRol(1L).nombre("ADMIN").build();
        when(usuarios.findByUsername("Admin1234")).thenReturn(Optional.of(usuario));
        when(rolUsuarios.findActiveByUsuarioId(1L)).thenReturn(Optional.of(RolUsuario.builder().usuario(usuario).rol(rol).build()));
        when(menuFunction.menuPorRol(1L)).thenReturn(List.of(MenuResponse.builder().nombre("Dashboard").build()));

        MenuApplicationService service = new MenuApplicationService(usuarios, rolUsuarios, menuFunction);

        assertThat(service.menuByAuthenticatedUser("Admin1234")).hasSize(1);
        verify(menuFunction).menuPorRol(1L);
    }
}
