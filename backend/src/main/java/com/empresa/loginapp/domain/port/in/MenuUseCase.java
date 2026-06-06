package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.shared.dto.response.MenuResponse;
import java.util.List;

public interface MenuUseCase {

    List<MenuResponse> menuByAuthenticatedUser(String username);
}
