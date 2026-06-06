package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.Sesion;
import java.util.*;

public interface SesionRepositoryPort {

    List<Sesion> findAll();

    List<Sesion> findByUsuario(Long idUsuario);

    List<Sesion> findFailed();

    Sesion save(Sesion sesion);

    Optional<Sesion> findActiveByUsuario(Long idUsuario);

    long countFailed();
}
