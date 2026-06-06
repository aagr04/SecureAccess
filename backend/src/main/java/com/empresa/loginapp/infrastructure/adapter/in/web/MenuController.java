package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.domain.port.in.MenuUseCase;
import com.empresa.loginapp.shared.dto.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuUseCase menuUseCase;

    @GetMapping
    public List<MenuResponse> menu(Principal principal) {
        return menuUseCase.menuByAuthenticatedUser(principal.getName());
    }
}
