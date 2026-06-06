package com.empresa.loginapp.infrastructure.adapter.out.persistence.adapter;

import com.empresa.loginapp.domain.port.out.MenuFunctionPort;
import com.empresa.loginapp.shared.dto.response.MenuResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MenuPersistenceAdapter implements MenuFunctionPort {
    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<MenuResponse> menuPorRol(Long idRol) {
        List<Object[]> rows = entityManager.createNativeQuery("select * from fn_menu_por_rol(:idRol)")
                .setParameter("idRol", idRol).getResultList();
        return rows.stream().map(r -> MenuResponse.builder()
                .idOpcion(((Number) r[0]).longValue()).nombre((String) r[1]).ruta((String) r[2])
                .icono((String) r[3]).orden(r[4] == null ? null : ((Number) r[4]).intValue()).build()).toList();
    }
}
