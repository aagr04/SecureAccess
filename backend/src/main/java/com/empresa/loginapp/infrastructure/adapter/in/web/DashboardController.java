package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.domain.port.in.DashboardUseCase;
import com.empresa.loginapp.shared.dto.response.DashboardResumenResponse;
import com.empresa.loginapp.shared.dto.response.SesionResponse;
import com.empresa.loginapp.shared.mapper.SesionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class DashboardController {

    private final DashboardUseCase dashboard;

    @GetMapping("/resumen")
    public DashboardResumenResponse resumen() {
        return dashboard.resumen();
    }

    @GetMapping("/sesiones-fallidas")
    public List<SesionResponse> sesionesFallidas() {
        return dashboard.sesionesFallidas().stream().map(SesionMapper::toResponse).toList();
    }
}
