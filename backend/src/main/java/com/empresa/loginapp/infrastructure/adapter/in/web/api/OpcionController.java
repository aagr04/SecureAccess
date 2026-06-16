package com.empresa.loginapp.infrastructure.adapter.in.web.api;

import com.empresa.loginapp.domain.port.in.OpcionUseCase;
import com.empresa.loginapp.shared.dto.request.OpcionRequest;
import com.empresa.loginapp.shared.dto.response.OpcionResponse;
import com.empresa.loginapp.shared.mapper.OpcionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/opciones")
@RequiredArgsConstructor

public class OpcionController {

    private final OpcionUseCase opciones;

    @GetMapping
    public List<OpcionResponse> findAll() {
        return opciones.findAll().stream().map(OpcionMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public OpcionResponse findById(@PathVariable Long id) {
        return OpcionMapper.toResponse(opciones.findById(id));
    }

    @PostMapping
    public OpcionResponse create(@Valid @RequestBody OpcionRequest request) {
        return OpcionMapper.toResponse(opciones.create(request));
    }

    @PutMapping("/{id}")
    public OpcionResponse update(@PathVariable Long id, @Valid @RequestBody OpcionRequest request) {
        return OpcionMapper.toResponse(opciones.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        opciones.delete(id);
    }
}
