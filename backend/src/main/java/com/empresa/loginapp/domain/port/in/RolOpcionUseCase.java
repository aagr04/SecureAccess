package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.RolOpcion;
import com.empresa.loginapp.shared.dto.request.RolOpcionRequest;
import java.util.List;

public interface RolOpcionUseCase {

    List<RolOpcion> findAll();

    RolOpcion findById(Long id);

    RolOpcion create(RolOpcionRequest request);

    RolOpcion update(Long id, RolOpcionRequest request);

    void delete(Long id);
}
