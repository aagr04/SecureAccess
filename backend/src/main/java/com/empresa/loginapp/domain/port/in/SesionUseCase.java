package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.Sesion;
import java.util.List;

public interface SesionUseCase {

    List<Sesion> findAll();

    List<Sesion> findByUsuario(Long idUsuario);

    Sesion findLastByUsuario(Long idUsuario);
}
