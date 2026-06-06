package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.Opcion;
import com.empresa.loginapp.domain.port.out.OpcionRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.OpcionPersistenceMapper;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.OpcionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OpcionPersistenceAdapter implements OpcionRepositoryPort {

    private final OpcionJpaRepository repository;

    private final OpcionPersistenceMapper mapper = new OpcionPersistenceMapper();

    public List<Opcion> findAllActive() {
        return repository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    public Optional<Opcion> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public Opcion save(Opcion opcion) {
        return mapper.toDomain(repository.save(mapper.toEntity(opcion)));
    }
}
