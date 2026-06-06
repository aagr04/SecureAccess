package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolUsuarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface RolUsuarioJpaRepository extends JpaRepository<RolUsuarioEntity, Long> {

    @EntityGraph(attributePaths = {"usuario", "usuario.persona", "rol"})
    Optional<RolUsuarioEntity> findFirstByUsuarioIdUsuarioAndActivoTrue(Long idUsuario);
}
