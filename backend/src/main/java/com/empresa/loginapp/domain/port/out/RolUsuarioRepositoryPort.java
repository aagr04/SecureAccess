package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.RolUsuario;
import java.util.*;

public interface RolUsuarioRepositoryPort {

    Optional<RolUsuario> findActiveByUsuarioId(Long idUsuario);

    RolUsuario save(RolUsuario rolUsuario);
}
