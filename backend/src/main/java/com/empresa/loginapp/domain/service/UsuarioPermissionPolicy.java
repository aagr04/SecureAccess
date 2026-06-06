package com.empresa.loginapp.domain.service;

import com.empresa.loginapp.domain.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPermissionPolicy {
    private static final String ADMIN = "ADMIN";

    public void assertCanUpdate(boolean actorAdmin, boolean targetAdmin, boolean sameUser) {
        if (actorAdmin) {
            if (targetAdmin) {
                throw new ForbiddenException("No se puede actualizar otro administrador.");
            }
            return;
        }
        if (!sameUser) {
            throw new ForbiddenException("Solo puede actualizar sus propios datos.");
        }
    }

    public void assertCanDelete(boolean actorAdmin, boolean targetAdmin) {
        if (!actorAdmin) {
            throw new ForbiddenException("No tiene permisos para realizar esta accion.");
        }
        if (targetAdmin) {
            throw new ForbiddenException("No se puede eliminar otro administrador.");
        }
    }

    public boolean isAdminRole(String roleName) {
        return ADMIN.equalsIgnoreCase(roleName);
    }
}
