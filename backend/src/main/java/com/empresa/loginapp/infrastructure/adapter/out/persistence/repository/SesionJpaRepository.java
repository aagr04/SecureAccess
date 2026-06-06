package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.SesionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SesionJpaRepository extends JpaRepository<SesionEntity, Long> {

    List<SesionEntity> findByUsuarioIdUsuario(Long idUsuario);

    Optional<SesionEntity> findFirstByUsuarioIdUsuarioAndActivaTrueOrderByFechaIngresoDesc(Long idUsuario);

    List<SesionEntity> findByExitosoFalse();

    long countByExitosoFalse();
}
