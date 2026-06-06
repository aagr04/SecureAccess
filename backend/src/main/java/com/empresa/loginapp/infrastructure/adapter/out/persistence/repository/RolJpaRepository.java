package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface RolJpaRepository extends JpaRepository<RolEntity, Long> {

    List<RolEntity> findByActivoTrue();

    Optional<RolEntity> findByNombreIgnoreCase(String nombre);
}
