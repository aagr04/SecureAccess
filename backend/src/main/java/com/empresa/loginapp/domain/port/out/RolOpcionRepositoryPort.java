package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.RolOpcion;
import java.util.*;

public interface RolOpcionRepositoryPort {

    List<RolOpcion> findAllActive();

    Optional<RolOpcion> findById(Long id);

    RolOpcion save(RolOpcion rolOpcion);
}
