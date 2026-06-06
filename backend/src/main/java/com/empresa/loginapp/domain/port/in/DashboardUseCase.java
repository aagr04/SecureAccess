package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.shared.dto.response.DashboardResumenResponse;
import java.util.List;

public interface DashboardUseCase {

    DashboardResumenResponse resumen();

    List<Sesion> sesionesFallidas();
}
