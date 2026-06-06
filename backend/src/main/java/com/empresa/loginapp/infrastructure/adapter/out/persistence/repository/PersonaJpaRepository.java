package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PersonaJpaRepository extends JpaRepository<PersonaEntity, Long> {

    List<PersonaEntity> findByActivoTrue();

    Optional<PersonaEntity> findByIdentificacion(String identificacion);
}
