package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.Rol;
import java.util.*;

public interface RolRepositoryPort {

    List<Rol> findAllActive();

    Optional<Rol> findById(Long id);

    Optional<Rol> findByNombre(String nombre);

    Rol save(Rol rol);
}
