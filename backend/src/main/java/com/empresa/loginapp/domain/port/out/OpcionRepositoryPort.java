package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.Opcion;
import java.util.*;

public interface OpcionRepositoryPort {

    List<Opcion> findAllActive();

    Optional<Opcion> findById(Long id);

    Opcion save(Opcion opcion);
}
