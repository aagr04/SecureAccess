package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.domain.port.out.UsuarioRepositoryPort;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.criteria.UsuarioCriteriaRepository;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.mapper.UsuarioPersistenceMapper;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import com.empresa.loginapp.shared.dto.request.UsuarioFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    private final UsuarioCriteriaRepository criteriaRepository;

    private final UsuarioPersistenceMapper mapper = new UsuarioPersistenceMapper();

    public List<Usuario> findAllActive() {
        return repository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    public Optional<Usuario> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public Optional<Usuario> findByCredential(String credential) {
        return repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(credential, credential).map(mapper::toDomain);
    }

    public Optional<Usuario> findByUsername(String username) {
        return repository.findByUsernameIgnoreCase(username).map(mapper::toDomain);
    }

    public boolean existsActiveByPersonaIdentificacion(String identificacion) {
        return repository.existsActiveByPersonaIdentificacion(identificacion);
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsernameIgnoreCase(username);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    public Set<String> findAllEmails() {
        return repository.findAllEmails();
    }

    public Usuario save(Usuario usuario) {
        return mapper.toDomain(repository.save(mapper.toEntity(usuario)));
    }

    public List<Usuario> filter(UsuarioFilterRequest filter) {
        return criteriaRepository.filter(filter).stream().map(mapper::toDomain).toList();
    }

    public long countAll() {
        return repository.count();
    }

    public long countByActivo(boolean activo) {
        return repository.countByActivo(activo);
    }

    public long countByStatus(String status) {
        return repository.countByStatus(status);
    }

    public long countBySesionActiva(boolean active) {
        return repository.countBySesionActiva(active);
    }
}
