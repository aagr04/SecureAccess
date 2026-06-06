package com.empresa.loginapp.domain.port.out;

import com.empresa.loginapp.domain.model.Persona;
import java.util.*;

public interface PersonaRepositoryPort {

    List<Persona> findAllActive();

    Optional<Persona> findById(Long id);

    Optional<Persona> findByIdentificacion(String identificacion);

    Persona save(Persona persona);
}
