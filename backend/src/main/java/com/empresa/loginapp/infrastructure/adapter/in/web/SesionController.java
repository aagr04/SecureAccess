package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.domain.port.in.SesionUseCase;
import com.empresa.loginapp.domain.port.in.UsuarioUseCase;
import com.empresa.loginapp.shared.dto.response.SesionResponse;
import com.empresa.loginapp.shared.mapper.SesionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor

public class SesionController {

    private final SesionUseCase sesiones;
    private final UsuarioUseCase usuarios;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<SesionResponse> findAll() {
        return sesiones.findAll().stream().map(SesionMapper::toResponse).toList();
    }

    @GetMapping("/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SesionResponse> findByUsuario(@PathVariable Long idUsuario) {
        return sesiones.findByUsuario(idUsuario).stream().map(SesionMapper::toResponse).toList();
    }

    @GetMapping("/me/last")
    @PreAuthorize("isAuthenticated()")
    public SesionResponse lastMe(Principal principal) {
        Long idUsuario = usuarios.findByUsername(principal.getName()).getIdUsuario();
        return SesionMapper.toResponse(sesiones.findLastByUsuario(idUsuario));
    }
}
