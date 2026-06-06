package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolOpcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface RolOpcionJpaRepository extends JpaRepository<RolOpcionEntity, Long> {

    List<RolOpcionEntity> findByActivoTrue();
}
