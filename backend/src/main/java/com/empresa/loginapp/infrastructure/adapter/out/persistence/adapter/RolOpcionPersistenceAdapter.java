package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.out.RolOpcionRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.*;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.*;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RolOpcionPersistenceAdapter implements RolOpcionRepositoryPort {

    private final RolOpcionJpaRepository repository;

    private final RolJpaRepository rolRepository;

    private final OpcionJpaRepository opcionRepository;

    private final RolPersistenceMapper rolMapper = new RolPersistenceMapper();

    private final OpcionPersistenceMapper opcionMapper = new OpcionPersistenceMapper();

    public List<RolOpcion> findAllActive() {
        return repository.findByActivoTrue().stream().map(this::toDomain).toList();
    }

    public Optional<RolOpcion> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public RolOpcion save(RolOpcion ro) {
        RolEntity rol = rolRepository.findById(ro.getRol().getIdRol()).orElseThrow();
        OpcionEntity opcion = opcionRepository.findById(ro.getOpcion().getIdOpcion()).orElseThrow();
        RolOpcionEntity entity = RolOpcionEntity.builder().idRolOpcion(ro.getIdRolOpcion()).rol(rol).opcion(opcion).activo(ro.getActivo()).build();
        return toDomain(repository.save(entity));
    }

    private RolOpcion toDomain(RolOpcionEntity e) {
        return RolOpcion.builder().idRolOpcion(e.getIdRolOpcion()).rol(rolMapper.toDomain(e.getRol())).opcion(opcionMapper.toDomain(e.getOpcion())).activo(e.getActivo()).build();
    }
}
