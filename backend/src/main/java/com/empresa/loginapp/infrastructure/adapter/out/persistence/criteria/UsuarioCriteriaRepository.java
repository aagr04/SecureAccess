package com.empresa.loginapp.infrastructure.adapter.out.persistence.criteria;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.*;
import com.empresa.loginapp.shared.dto.request.UsuarioFilterRequest;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UsuarioCriteriaRepository {
    private final EntityManager entityManager;

    public List<UsuarioEntity> filter(UsuarioFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
        Root<UsuarioEntity> usuario = cq.from(UsuarioEntity.class);
        usuario.fetch("persona", JoinType.LEFT);
        Join<UsuarioEntity, PersonaEntity> persona = usuario.join("persona", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isTrue(usuario.get("activo")));
        like(cb, predicates, persona.get("nombres"), filter.getNombres());
        like(cb, predicates, persona.get("apellidos"), filter.getApellidos());
        eq(cb, predicates, persona.get("identificacion"), filter.getIdentificacion());
        like(cb, predicates, usuario.get("username"), filter.getUsername());
        like(cb, predicates, usuario.get("email"), filter.getEmail());
        eq(cb, predicates, usuario.get("status"), filter.getStatus());
        if (hasFilterValue(filter.getRol())) {
            Subquery<Long> sq = cq.subquery(Long.class);
            Root<RolUsuarioEntity> ru = sq.from(RolUsuarioEntity.class);
            Join<RolUsuarioEntity, RolEntity> rol = ru.join("rol");
            sq.select(ru.get("usuario").get("idUsuario"))
                    .where(cb.and(cb.isTrue(ru.get("activo")), cb.isTrue(rol.get("activo")),
                            cb.equal(cb.lower(rol.get("nombre")), clean(filter.getRol()).toLowerCase())));
            predicates.add(usuario.get("idUsuario").in(sq));
        }
        cq.distinct(true);
        cq.where(predicates.toArray(Predicate[]::new));
        cq.orderBy(cb.asc(usuario.get("idUsuario")));
        return entityManager.createQuery(cq).getResultList();
    }

    private void like(CriteriaBuilder cb, List<Predicate> predicates, Path<String> path, String value) {
        if (hasFilterValue(value)) predicates.add(cb.like(cb.lower(path), "%" + clean(value).toLowerCase() + "%"));
    }

    private void eq(CriteriaBuilder cb, List<Predicate> predicates, Path<String> path, String value) {
        if (hasFilterValue(value)) predicates.add(cb.equal(cb.lower(path), clean(value).toLowerCase()));
    }

    private boolean hasFilterValue(String value) {
        return value != null && !value.trim().isEmpty() && !"Seleccione".equalsIgnoreCase(value.trim());
    }

    private String clean(String value) {
        return value.trim();
    }
}
