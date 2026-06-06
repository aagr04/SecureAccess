package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.out.RolUsuarioRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.*;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.*;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RolUsuarioPersistenceAdapter implements RolUsuarioRepositoryPort {

    private final RolUsuarioJpaRepository repository;

    private final UsuarioJpaRepository usuarioRepository;

    private final RolJpaRepository rolRepository;

    private final UsuarioPersistenceMapper usuarioMapper = new UsuarioPersistenceMapper();

    private final RolPersistenceMapper rolMapper = new RolPersistenceMapper();

    public Optional<RolUsuario> findActiveByUsuarioId(Long idUsuario) {
        return repository.findFirstByUsuarioIdUsuarioAndActivoTrue(idUsuario).map(this::toDomain);
    }

    public RolUsuario save(RolUsuario rolUsuario) {
        UsuarioEntity usuario = usuarioRepository.findById(rolUsuario.getUsuario().getIdUsuario()).orElseThrow();
        RolEntity rol = rolRepository.findById(rolUsuario.getRol().getIdRol()).orElseThrow();
        RolUsuarioEntity entity = RolUsuarioEntity.builder().idRolUsuario(rolUsuario.getIdRolUsuario()).usuario(usuario).rol(rol).activo(rolUsuario.getActivo()).build();
        return toDomain(repository.save(entity));
    }

    private RolUsuario toDomain(RolUsuarioEntity e) {
        return RolUsuario.builder().idRolUsuario(e.getIdRolUsuario()).usuario(usuarioMapper.toDomain(e.getUsuario())).rol(rolMapper.toDomain(e.getRol())).activo(e.getActivo()).build();
    }
}
