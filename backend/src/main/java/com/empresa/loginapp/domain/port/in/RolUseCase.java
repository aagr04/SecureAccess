package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.Rol;
import com.empresa.loginapp.shared.dto.request.SimpleNameRequest;
import java.util.List;

public interface RolUseCase {

    List<Rol> findAll();

    Rol findById(Long id);

    Rol create(SimpleNameRequest request);

    Rol update(Long id, SimpleNameRequest request);

    void delete(Long id);
}
