package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.NotFoundException;
import com.empresa.loginapp.domain.model.Persona;
import com.empresa.loginapp.domain.port.in.PersonaUseCase;
import com.empresa.loginapp.domain.port.out.PersonaRepositoryPort;
import com.empresa.loginapp.domain.service.IdentificacionValidator;
import com.empresa.loginapp.shared.dto.request.PersonaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaApplicationService implements PersonaUseCase {
    private final PersonaRepositoryPort personas;
    private final IdentificacionValidator identificacionValidator;

    public List<Persona> findAll() {
        return personas.findAllActive();
    }

    public Persona findById(Long id) {
        return personas.findById(id).filter(p -> Boolean.TRUE.equals(p.getActivo())).orElseThrow(() -> new NotFoundException("Persona no encontrada"));
    }

    @Transactional
    public Persona create(PersonaRequest r) {
        identificacionValidator.validate(r.getIdentificacion());
        return personas.save(Persona.builder().nombres(r.getNombres()).apellidos(r.getApellidos()).identificacion(r.getIdentificacion()).fechaNacimiento(r.getFechaNacimiento()).activo(true).build());
    }

    @Transactional
    public Persona update(Long id, PersonaRequest r) {
        Persona p = findById(id);
        identificacionValidator.validate(r.getIdentificacion());
        p.setNombres(r.getNombres()); p.setApellidos(r.getApellidos()); p.setIdentificacion(r.getIdentificacion()); p.setFechaNacimiento(r.getFechaNacimiento());
        return personas.save(p);
    }

    @Transactional
    public void delete(Long id) {
        Persona p = findById(id); p.setActivo(false); personas.save(p);
    }
}
