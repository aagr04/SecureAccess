package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.domain.port.out.SesionRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.SesionPersistenceMapper;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component @RequiredArgsConstructor
public class SesionPersistenceAdapter implements SesionRepositoryPort {

    private final SesionJpaRepository repository;

    private final UsuarioJpaRepository usuarioRepository;

    private final SesionPersistenceMapper mapper = new SesionPersistenceMapper();

    public List<Sesion> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    public List<Sesion> findByUsuario(Long idUsuario) {
        return repository.findByUsuarioIdUsuario(idUsuario).stream().map(mapper::toDomain).toList();
    }

    public List<Sesion> findFailed() {
        return repository.findByExitosoFalse().stream().map(mapper::toDomain).toList();
    }

    public Sesion save(Sesion sesion) {
        UsuarioEntity usuario = usuarioRepository.findById(sesion.getIdUsuario()).orElseThrow();
        return mapper.toDomain(repository.save(mapper.toEntity(sesion, usuario)));
    }

    public Optional<Sesion> findActiveByUsuario(Long idUsuario) {
        return repository.findFirstByUsuarioIdUsuarioAndActivaTrueOrderByFechaIngresoDesc(idUsuario).map(mapper::toDomain);
    }

    public long countFailed() {
        return repository.countByExitosoFalse();
    }
}
