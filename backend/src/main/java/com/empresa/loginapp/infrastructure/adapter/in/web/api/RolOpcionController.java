package com.empresa.loginapp.infrastructure.adapter.in.web.api;

import com.empresa.loginapp.domain.port.in.RolOpcionUseCase;
import com.empresa.loginapp.shared.dto.request.RolOpcionRequest;
import com.empresa.loginapp.shared.dto.response.RolOpcionResponse;
import com.empresa.loginapp.shared.mapper.RolOpcionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rol-opciones")
@RequiredArgsConstructor

public class RolOpcionController {

    private final RolOpcionUseCase rolOpciones;

    @GetMapping
    public List<RolOpcionResponse> findAll() {
        return rolOpciones.findAll().stream().map(RolOpcionMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public RolOpcionResponse findById(@PathVariable Long id) {
        return RolOpcionMapper.toResponse(rolOpciones.findById(id));
    }

    @PostMapping
    public RolOpcionResponse create(@Valid @RequestBody RolOpcionRequest request) {
        return RolOpcionMapper.toResponse(rolOpciones.create(request));
    }

    @PutMapping("/{id}")
    public RolOpcionResponse update(@PathVariable Long id, @Valid @RequestBody RolOpcionRequest request) {
        return RolOpcionMapper.toResponse(rolOpciones.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { rolOpciones.delete(id);
    }
}
