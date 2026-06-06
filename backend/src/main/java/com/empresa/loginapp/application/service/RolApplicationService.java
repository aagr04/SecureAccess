package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.NotFoundException;
import com.empresa.loginapp.domain.model.Rol;
import com.empresa.loginapp.domain.port.in.RolUseCase;
import com.empresa.loginapp.domain.port.out.RolRepositoryPort;
import com.empresa.loginapp.shared.dto.request.SimpleNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolApplicationService implements RolUseCase {
    private final RolRepositoryPort roles;

    public List<Rol> findAll() {
        return roles.findAllActive();
    }

    public Rol findById(Long id) {
        return roles.findById(id).filter(r -> Boolean.TRUE.equals(r.getActivo())).orElseThrow(() -> new NotFoundException("Rol no encontrado"));
    }

    @Transactional
    public Rol create(SimpleNameRequest r) {
        return roles.save(Rol.builder().nombre(r.getNombre()).activo(true).build());
    }

    @Transactional
    public Rol update(Long id, SimpleNameRequest r) {
        Rol rol = findById(id); rol.setNombre(r.getNombre()); return roles.save(rol);
    }

    @Transactional
    public void delete(Long id) {
        Rol rol = findById(id); rol.setActivo(false); roles.save(rol);
    }
}
