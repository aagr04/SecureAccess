package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.Rol;
import com.empresa.loginapp.domain.port.out.RolRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.RolPersistenceMapper;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.RolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RolPersistenceAdapter implements RolRepositoryPort {

    private final RolJpaRepository repository;

    private final RolPersistenceMapper mapper = new RolPersistenceMapper();

    public List<Rol> findAllActive() {
        return repository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    public Optional<Rol> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public Optional<Rol> findByNombre(String nombre) {
        return repository.findByNombreIgnoreCase(nombre).map(mapper::toDomain);
    }

    public Rol save(Rol rol) {
        return mapper.toDomain(repository.save(mapper.toEntity(rol)));
    }
}
