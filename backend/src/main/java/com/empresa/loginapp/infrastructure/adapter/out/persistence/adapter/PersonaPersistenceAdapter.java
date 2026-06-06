package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.Persona;
import com.empresa.loginapp.domain.port.out.PersonaRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.PersonaPersistenceMapper;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.PersonaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor

public class PersonaPersistenceAdapter implements PersonaRepositoryPort {

    private final PersonaJpaRepository repository;

    private final PersonaPersistenceMapper mapper = new PersonaPersistenceMapper();

    public List<Persona> findAllActive() {
        return repository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    public Optional<Persona> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public Optional<Persona> findByIdentificacion(String identificacion) {
        return repository.findByIdentificacion(identificacion).map(mapper::toDomain);
    }

    public Persona save(Persona persona) {
        return mapper.toDomain(repository.save(mapper.toEntity(persona)));
    }

}
