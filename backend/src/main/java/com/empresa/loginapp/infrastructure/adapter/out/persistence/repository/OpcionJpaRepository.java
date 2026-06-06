package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.OpcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpcionJpaRepository extends JpaRepository<OpcionEntity, Long> {

    List<OpcionEntity> findByActivoTrue();
}
