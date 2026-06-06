package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.shared.dto.response.MenuResponse;
import java.util.List;

public interface MenuFunctionPort {

    List<MenuResponse> menuPorRol(Long idRol);
}
