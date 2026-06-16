package com.empresa.loginapp.infrastructure.adapter.in.web.api;

import com.empresa.loginapp.domain.port.in.RolUseCase;
import com.empresa.loginapp.shared.dto.request.RolRequest;
import com.empresa.loginapp.shared.dto.response.RolResponse;
import com.empresa.loginapp.shared.mapper.RolMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolUseCase roles;

    @GetMapping
    public List<RolResponse> findAll() {
        return roles.findAll().stream().map(RolMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public RolResponse findById(@PathVariable Long id) {
        return RolMapper.toResponse(roles.findById(id));
    }

    @PostMapping
    public RolResponse create(@Valid @RequestBody RolRequest request) {
        return RolMapper.toResponse(roles.create(request));
    }

    @PutMapping("/{id}")
    public RolResponse update(@PathVariable Long id, @Valid @RequestBody RolRequest request) {
        return RolMapper.toResponse(roles.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roles.delete(id);
    }
}
