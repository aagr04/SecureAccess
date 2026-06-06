package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.shared.dto.response.DashboardResumenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardApplicationServiceTest {
    @Mock
    DashboardRepositoryPort dashboard;
    @Mock
    SesionRepositoryPort sesiones;

    @Test
    void resumenYSesionesFallidas() {
        when(dashboard.resumen()).thenReturn(DashboardResumenResponse.builder().totalUsuarios(2).usuariosActivos(1).build());
        when(sesiones.findFailed()).thenReturn(List.of(Sesion.builder().idSesion(1L).exitoso(false).build()));
        DashboardApplicationService service = new DashboardApplicationService(dashboard, sesiones);

        assertThat(service.resumen().getTotalUsuarios()).isEqualTo(2);
        assertThat(service.sesionesFallidas()).hasSize(1);
    }
}
