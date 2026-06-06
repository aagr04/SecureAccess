package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.NotFoundException;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.in.RolOpcionUseCase;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.shared.dto.request.RolOpcionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolOpcionApplicationService implements RolOpcionUseCase {
    private final RolOpcionRepositoryPort rolOpciones;
    private final RolRepositoryPort roles;
    private final OpcionRepositoryPort opciones;

    public List<RolOpcion> findAll() {
        return rolOpciones.findAllActive();
    }

    public RolOpcion findById(Long id) {
        return rolOpciones.findById(id).filter(ro -> Boolean.TRUE.equals(ro.getActivo())).orElseThrow(() -> new NotFoundException("Rol-opcion no encontrado"));
    }

    @Transactional
    public RolOpcion create(RolOpcionRequest r) {
        return rolOpciones.save(build(null, r));
    }

    @Transactional
    public RolOpcion update(Long id, RolOpcionRequest r) {
        return rolOpciones.save(build(id, r));
    }

    @Transactional
    public void delete(Long id) {
        RolOpcion ro = findById(id); ro.setActivo(false); rolOpciones.save(ro);
    }

    private RolOpcion build(Long id, RolOpcionRequest r) {
        Rol rol = roles.findById(r.getIdRol()).orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        Opcion opcion = opciones.findById(r.getIdOpcion()).orElseThrow(() -> new NotFoundException("Opcion no encontrada"));
        return RolOpcion.builder().idRolOpcion(id).rol(rol).opcion(opcion).activo(true).build();
    }
}
