package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.domain.port.in.DashboardUseCase;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.shared.dto.response.DashboardResumenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardApplicationService implements DashboardUseCase {
    private final DashboardRepositoryPort dashboard;
    private final SesionRepositoryPort sesiones;

    public DashboardResumenResponse resumen() {
        return dashboard.resumen();
    }

    public List<Sesion> sesionesFallidas() {
        return sesiones.findFailed();
    }
}
