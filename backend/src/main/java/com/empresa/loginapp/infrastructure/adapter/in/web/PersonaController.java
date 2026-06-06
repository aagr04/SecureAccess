package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.domain.port.in.PersonaUseCase;
import com.empresa.loginapp.shared.dto.request.PersonaRequest;
import com.empresa.loginapp.shared.dto.response.PersonaResponse;
import com.empresa.loginapp.shared.mapper.PersonaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor

public class PersonaController {

    private final PersonaUseCase personas;

    @GetMapping
    public List<PersonaResponse> findAll() {
        return personas.findAll().stream().map(PersonaMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PersonaResponse findById(@PathVariable Long id) {
        return PersonaMapper.toResponse(personas.findById(id));
    }

    @PostMapping
    public PersonaResponse create(@Valid @RequestBody PersonaRequest request) {
        return PersonaMapper.toResponse(personas.create(request));
    }

    @PutMapping("/{id}")
    public PersonaResponse update(@PathVariable Long id, @Valid @RequestBody PersonaRequest request) {
        return PersonaMapper.toResponse(personas.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        personas.delete(id);
    }
}
