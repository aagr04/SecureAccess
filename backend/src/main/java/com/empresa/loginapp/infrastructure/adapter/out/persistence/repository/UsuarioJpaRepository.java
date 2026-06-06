package com.empresa.loginapp.infrastructure.adapter.out.persistence.repository;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    @EntityGraph(attributePaths = "persona")
    List<UsuarioEntity> findByActivoTrue();

    @EntityGraph(attributePaths = "persona")
    Optional<UsuarioEntity> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    @EntityGraph(attributePaths = "persona")
    Optional<UsuarioEntity> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    @Query("select count(u)>0 from UsuarioEntity u where u.activo=true and u.persona.identificacion=:identificacion")

    boolean existsActiveByPersonaIdentificacion(@Param("identificacion") String identificacion);

    @Query("select u.email from UsuarioEntity u")

    Set<String> findAllEmails();

    long countByActivo(boolean activo);

    long countByStatus(String status);

    long countBySesionActiva(boolean active);
}
