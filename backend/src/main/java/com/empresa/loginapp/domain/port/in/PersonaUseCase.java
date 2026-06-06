package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.Persona;
import com.empresa.loginapp.shared.dto.request.PersonaRequest;
import java.util.List;

public interface PersonaUseCase {

    List<Persona> findAll();

    Persona findById(Long id);

    Persona create(PersonaRequest request);

    Persona update(Long id, PersonaRequest request);

    void delete(Long id);
}
