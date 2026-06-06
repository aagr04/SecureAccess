package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.NotFoundException;
import com.empresa.loginapp.domain.model.Opcion;
import com.empresa.loginapp.domain.port.in.OpcionUseCase;
import com.empresa.loginapp.domain.port.out.OpcionRepositoryPort;
import com.empresa.loginapp.shared.dto.request.OpcionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpcionApplicationService implements OpcionUseCase {
    private final OpcionRepositoryPort opciones;

    public List<Opcion> findAll() {
        return opciones.findAllActive();
    }

    public Opcion findById(Long id) {
        return opciones.findById(id).filter(o -> Boolean.TRUE.equals(o.getActivo())).orElseThrow(() -> new NotFoundException("Opcion no encontrada"));
    }

    @Transactional
    public Opcion create(OpcionRequest r) {
        return opciones.save(Opcion.builder().nombre(r.getNombre()).ruta(r.getRuta()).icono(r.getIcono()).orden(r.getOrden()).activo(true).build());
    }

    @Transactional
    public Opcion update(Long id, OpcionRequest r) {
        Opcion o = findById(id); o.setNombre(r.getNombre()); o.setRuta(r.getRuta()); o.setIcono(r.getIcono()); o.setOrden(r.getOrden()); return opciones.save(o);
    }

    @Transactional
    public void delete(Long id) {
        Opcion o = findById(id); o.setActivo(false); opciones.save(o);
    }
}
