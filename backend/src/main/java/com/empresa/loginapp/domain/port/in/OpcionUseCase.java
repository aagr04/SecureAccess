package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.Opcion;
import com.empresa.loginapp.shared.dto.request.OpcionRequest;
import java.util.List;

public interface OpcionUseCase {

    List<Opcion> findAll();

    Opcion findById(Long id);

    Opcion create(OpcionRequest request);

    Opcion update(Long id, OpcionRequest request);

    void delete(Long id);
}
