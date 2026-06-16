package com.empresa.loginapp.infrastructure.adapter.in.web.api;

import com.empresa.loginapp.domain.port.in.UsuarioUseCase;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.*;
import com.empresa.loginapp.shared.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioUseCase usuarios;

    @GetMapping
    public List<UsuarioResponse> findAll() {
        return usuarios.findAll().stream().map(UsuarioMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse findById(@PathVariable Long id) {
        return UsuarioMapper.toResponse(usuarios.findById(id));
    }

    @GetMapping("/me")
    public UsuarioResponse me(Principal principal) {
        return UsuarioMapper.toResponse(usuarios.findByUsername(principal.getName()));
    }

    @PostMapping
    public UsuarioResponse create(@Valid @RequestBody UsuarioRequest request) {
        return UsuarioMapper.toResponse(usuarios.create(request));
    }

    @PutMapping("/{id}")
    public UsuarioResponse update(Principal principal, @PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return UsuarioMapper.toResponse(usuarios.update(principal.getName(), id, request));
    }

    @PutMapping("/me")
    public UsuarioResponse updateMe(Principal principal, @Valid @RequestBody UsuarioRequest request) {
        return UsuarioMapper.toResponse(usuarios.updateOwnProfile(principal.getName(), request));
    }

    @DeleteMapping("/{id}")
    public void delete(Principal principal, @PathVariable Long id) {
        usuarios.delete(principal.getName(), id);
    }

    @PatchMapping("/{id}/estado") @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse estado(Principal principal, @PathVariable Long id, @Valid @RequestBody CambiarEstadoUsuarioRequest request) {
        return UsuarioMapper.toResponse(usuarios.changeEstado(principal.getName(), id, request));
    }

    @PostMapping("/bulk") @PreAuthorize("hasRole('ADMIN')")
    public BulkUploadResponse bulk(@RequestParam("file") MultipartFile file) throws IOException {
        return usuarios.bulkUpload(BulkUploadRequest.builder().filename(file.getOriginalFilename()).content(file.getBytes()).build());
    }

    @GetMapping("/filter") @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponse> filter(UsuarioFilterRequest filter) {
        return usuarios.filter(filter).stream().map(UsuarioMapper::toResponse).toList();
    }
}
