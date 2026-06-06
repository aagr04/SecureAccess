package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.shared.dto.request.UsuarioFilterRequest;
import java.util.*;

public interface UsuarioRepositoryPort {

    List<Usuario> findAllActive();

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByCredential(String credential);

    Optional<Usuario> findByUsername(String username);

    boolean existsActiveByPersonaIdentificacion(String identificacion);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Set<String> findAllEmails();

    Usuario save(Usuario usuario);

    List<Usuario> filter(UsuarioFilterRequest filter);

    long countAll();

    long countByActivo(boolean activo);

    long countByStatus(String status);

    long countBySesionActiva(boolean active);
}
