package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.domain.exception.NotFoundException;
import com.empresa.loginapp.domain.port.in.SesionUseCase;
import com.empresa.loginapp.domain.port.out.SesionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionApplicationService implements SesionUseCase {
    private final SesionRepositoryPort sesiones;

    public List<Sesion> findAll() {
        return sesiones.findAll();
    }

    public List<Sesion> findByUsuario(Long idUsuario) {
        return sesiones.findByUsuario(idUsuario);
    }

    public Sesion findLastByUsuario(Long idUsuario) {
        return sesiones.findByUsuario(idUsuario).stream()
                .filter(s -> s.getFechaIngreso() != null)
                .max(java.util.Comparator.comparing(Sesion::getFechaIngreso))
                .orElseThrow(() -> new NotFoundException("No existen sesiones registradas"));
    }
}
