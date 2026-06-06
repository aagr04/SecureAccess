package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.*;
import com.empresa.loginapp.domain.model.*;
import com.empresa.loginapp.domain.port.in.UsuarioUseCase;
import com.empresa.loginapp.domain.port.out.*;
import com.empresa.loginapp.domain.service.*;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.BulkUploadResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service @RequiredArgsConstructor
public class UsuarioApplicationService implements UsuarioUseCase {
    private final UsuarioRepositoryPort usuarios;
    private final PersonaRepositoryPort personas;
    private final RolRepositoryPort roles;
    private final RolUsuarioRepositoryPort rolUsuarios;
    private final PasswordEncoder passwordEncoder;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;
    private final IdentificacionValidator identificacionValidator;
    private final EmailGeneratorService emailGenerator;
    private final UsuarioPermissionPolicy permissionPolicy;

    public List<Usuario> findAll() { return usuarios.findAllActive().stream().map(this::withActiveRole).toList(); }
    public Usuario findById(Long id) { return withActiveRole(activeUsuario(id)); }
    public Usuario findByUsername(String username) { return withActiveRole(activeUsuario(username)); }

    @Transactional
    public Usuario create(UsuarioRequest r) {
        validateUser(r, null);
        Persona persona = resolvePersona(r);
        String email = resolveEmail(r, persona);
        Usuario usuario = usuarios.save(Usuario.builder().username(r.getUsername()).password(passwordEncoder.encode(r.getPassword()))
                .email(email).status(r.getStatus() == null ? UsuarioStatus.ACTIVO : r.getStatus()).activo(true).intentosFallidos(0)
                .sesionActiva(false).persona(persona).build());
        if (r.getIdRol() != null) assignRole(usuario, r.getIdRol());
        else assignRole(usuario, r.getRol());
        return withActiveRole(usuario);
    }

    @Transactional
    public Usuario update(String authenticatedUsername, Long id, UsuarioRequest r) {
        Usuario actor = activeUsuario(authenticatedUsername);
        Usuario current = activeUsuario(id);
        boolean actorAdmin = isAdmin(actor);
        permissionPolicy.assertCanUpdate(actorAdmin, isAdmin(current), isSameUser(actor, current));
        if (!current.getUsername().equalsIgnoreCase(r.getUsername()) && usuarios.existsByUsername(r.getUsername())) throw new BusinessException("El nombre de usuario ya existe");
        usernameValidator.validate(r.getUsername());
        if (r.getPassword() != null && !r.getPassword().isBlank()) {
            passwordValidator.validate(r.getPassword());
            current.setPassword(passwordEncoder.encode(r.getPassword()));
        }
        updatePersona(current, r);
        current.setUsername(r.getUsername());
        if (actorAdmin) current.setStatus(r.getStatus() == null ? current.getStatus() : r.getStatus());
        return withActiveRole(usuarios.save(current));
    }

    @Transactional
    public Usuario updateOwnProfile(String username, UsuarioRequest r) {
        Usuario current = activeUsuario(username);
        if (!current.getUsername().equalsIgnoreCase(r.getUsername()) && usuarios.existsByUsername(r.getUsername())) throw new BusinessException("El nombre de usuario ya existe");
        usernameValidator.validate(r.getUsername());
        if (r.getPassword() != null && !r.getPassword().isBlank()) {
            passwordValidator.validate(r.getPassword());
            current.setPassword(passwordEncoder.encode(r.getPassword()));
        }
        Persona persona = current.getPersona();
        if (persona == null) throw new BusinessException("Usuario sin datos personales");
        persona.setNombres(r.getNombres());
        persona.setApellidos(r.getApellidos());
        persona.setFechaNacimiento(r.getFechaNacimiento());
        if (r.getIdentificacion() != null && !r.getIdentificacion().equals(persona.getIdentificacion())) {
            identificacionValidator.validate(r.getIdentificacion());
            if (usuarios.existsActiveByPersonaIdentificacion(r.getIdentificacion())) throw new BusinessException("Ya existe una cuenta registrada con esta identificacion");
            persona.setIdentificacion(r.getIdentificacion());
        }
        current.setUsername(r.getUsername());
        current.setPersona(personas.save(persona));
        return withActiveRole(usuarios.save(current));
    }

    @Transactional
    public void delete(String authenticatedUsername, Long id) {
        Usuario actor = activeUsuario(authenticatedUsername);
        Usuario u = activeUsuario(id);
        permissionPolicy.assertCanDelete(isAdmin(actor), isAdmin(u));
        u.setActivo(false);
        u.setStatus(UsuarioStatus.INACTIVO);
        u.setSesionActiva(false);
        usuarios.save(u);
        deactivatePersonaIfUnused(u);
    }

    @Transactional
    public Usuario changeEstado(String authenticatedUsername, Long id, EstadoUsuarioRequest r) {
        Usuario actor = activeUsuario(authenticatedUsername);
        Usuario u = activeUsuario(id);
        permissionPolicy.assertCanUpdate(isAdmin(actor), isAdmin(u), isSameUser(actor, u));
        u.setStatus(r.getStatus());
        if (r.getActivo() != null) u.setActivo(r.getActivo());
        return withActiveRole(usuarios.save(u));
    }

    public List<Usuario> filter(UsuarioFilterRequest filter) {
        return usuarios.filter(filter == null ? new UsuarioFilterRequest() : filter).stream().map(this::withActiveRole).toList();
    }

    @Transactional
    public BulkUploadResponse bulkUpload(BulkUploadRequest file) {
        validateBulkFile(file);
        List<UsuarioRequest> rows = parse(file);
        BulkUploadResponse response = BulkUploadResponse.builder().totalProcesados(rows.size()).build();
        int rowNumber = 1;
        for (UsuarioRequest row : rows) {
            try {
                create(row);
                response.setExitosos(response.getExitosos() + 1);
            } catch (Exception ex) {
                response.setFallidos(response.getFallidos() + 1);
                response.getErroresPorFila().add("Fila " + rowNumber + ": " + ex.getMessage());
            }
            rowNumber++;
        }
        return response;
    }

    private void validateUser(UsuarioRequest r, Long id) {
        usernameValidator.validate(r.getUsername());
        passwordValidator.validate(r.getPassword());
        if (usuarios.existsByUsername(r.getUsername())) throw new BusinessException("El nombre de usuario ya existe");
        if (r.getIdentificacion() != null) {
            identificacionValidator.validate(r.getIdentificacion());
            if (usuarios.existsActiveByPersonaIdentificacion(r.getIdentificacion())) throw new BusinessException("Ya existe una cuenta registrada con esta identificacion");
        }
    }

    private Persona resolvePersona(UsuarioRequest r) {
        if (r.getIdPersona() != null) return personas.findById(r.getIdPersona()).orElseThrow(() -> new NotFoundException("Persona no encontrada"));
        if (r.getIdentificacion() == null) throw new BusinessException("Debe enviar idPersona o datos de persona");
        Persona persona = personas.findByIdentificacion(r.getIdentificacion()).orElse(Persona.builder()
                .nombres(r.getNombres()).apellidos(r.getApellidos()).identificacion(r.getIdentificacion()).fechaNacimiento(r.getFechaNacimiento()).activo(true).build());
        return personas.save(persona);
    }

    private String resolveEmail(UsuarioRequest r, Persona persona) {
        String email = emailGenerator.generate(persona.getNombres(), persona.getApellidos(), usuarios.findAllEmails());
        if (usuarios.existsByEmail(email)) throw new BusinessException("Email duplicado");
        return email;
    }

    private void assignRole(Usuario usuario, Long idRol) {
        Rol rol = roles.findById(idRol).orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        rolUsuarios.save(RolUsuario.builder().usuario(usuario).rol(rol).activo(true).build());
    }

    private void assignRole(Usuario usuario, String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank()) return;
        Rol rol = roles.findByNombre(nombreRol).orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        rolUsuarios.save(RolUsuario.builder().usuario(usuario).rol(rol).activo(true).build());
    }

    private Usuario activeUsuario(Long id) {
        return usuarios.findById(id).filter(u -> Boolean.TRUE.equals(u.getActivo())).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private Usuario activeUsuario(String username) {
        return usuarios.findByUsername(username).filter(u -> Boolean.TRUE.equals(u.getActivo())).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private Usuario withActiveRole(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() == null) return usuario;
        rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario())
                .map(ru -> ru.getRol() == null ? null : ru.getRol().getNombre())
                .ifPresent(usuario::setRol);
        return usuario;
    }

    private boolean isAdmin(Usuario usuario) {
        return rolUsuarios.findActiveByUsuarioId(usuario.getIdUsuario())
                .map(ru -> ru.getRol() != null && permissionPolicy.isAdminRole(ru.getRol().getNombre()))
                .orElse(false);
    }

    private boolean isSameUser(Usuario actor, Usuario target) {
        return Objects.equals(actor.getIdUsuario(), target.getIdUsuario());
    }

    private void deactivatePersonaIfUnused(Usuario usuario) {
        Persona persona = usuario.getPersona();
        if (persona == null || persona.getIdPersona() == null) return;
        if (usuarios.existsAnotherActiveByPersonaId(persona.getIdPersona(), usuario.getIdUsuario())) return;
        persona.setActivo(false);
        personas.save(persona);
    }

    private void updatePersona(Usuario usuario, UsuarioRequest r) {
        Persona persona = usuario.getPersona();
        if (persona == null) throw new BusinessException("Usuario sin datos personales");
        persona.setNombres(r.getNombres());
        persona.setApellidos(r.getApellidos());
        persona.setFechaNacimiento(r.getFechaNacimiento());
        if (r.getIdentificacion() != null && !r.getIdentificacion().equals(persona.getIdentificacion())) {
            identificacionValidator.validate(r.getIdentificacion());
            if (usuarios.existsActiveByPersonaIdentificacion(r.getIdentificacion())) throw new BusinessException("Ya existe una cuenta registrada con esta identificacion");
            persona.setIdentificacion(r.getIdentificacion());
        }
        usuario.setPersona(personas.save(persona));
    }

    private void validateBulkFile(BulkUploadRequest file) {
        if (file == null || file.getContent() == null || file.getContent().length == 0) throw new BusinessException("Archivo vacio");
        String name = file.getFilename() == null ? "" : file.getFilename().toLowerCase();
        if (!(name.endsWith(".csv") || name.endsWith(".xlsx") || name.endsWith(".xls"))) throw new BusinessException("Extension invalida. Solo se permite .xlsx, .xls o .csv");
    }

    private List<UsuarioRequest> parse(BulkUploadRequest file) {
        String name = file.getFilename() == null ? "" : file.getFilename().toLowerCase();
        try {
            if (name.endsWith(".xlsx") || name.endsWith(".xls")) return parseExcel(file.getContent());
            return parseCsv(file.getContent());
        } catch (Exception ex) {
            throw new BusinessException("No se pudo leer archivo bulk: " + ex.getMessage());
        }
    }

    private List<UsuarioRequest> parseCsv(byte[] content) {
        List<UsuarioRequest> rows = new ArrayList<>();
        String text = new String(content, StandardCharsets.UTF_8);
        text.lines().skip(1).filter(l -> !l.isBlank()).forEach(line -> rows.add(toRequest(line.split(","))));
        return rows;
    }

    private List<UsuarioRequest> parseExcel(byte[] content) throws IOException {
        List<UsuarioRequest> rows = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) rows.add(toRequest(new String[]{cell(row,0), cell(row,1), cell(row,2), cell(row,3), cell(row,4), cell(row,5), cell(row,6)}));
            }
        }
        return rows;
    }

    private UsuarioRequest toRequest(String[] v) {
        UsuarioRequest r = new UsuarioRequest();
        r.setNombres(v.length > 0 ? v[0].trim() : null); r.setApellidos(v.length > 1 ? v[1].trim() : null);
        r.setIdentificacion(v.length > 2 ? v[2].trim() : null);
        boolean hasFechaNacimiento = v.length > 6 && v[3] != null && v[3].trim().matches("\\d{4}-\\d{2}-\\d{2}");
        if (hasFechaNacimiento) r.setFechaNacimiento(java.time.LocalDate.parse(v[3].trim()));
        int usernameIndex = hasFechaNacimiento ? 4 : 3;
        int passwordIndex = hasFechaNacimiento ? 5 : 4;
        int rolIndex = hasFechaNacimiento ? 6 : 5;
        r.setUsername(v.length > usernameIndex ? v[usernameIndex].trim() : null);
        r.setPassword(v.length > passwordIndex ? v[passwordIndex].trim() : null);
        r.setStatus("ACTIVO");
        if (v.length > rolIndex && !v[rolIndex].isBlank()) r.setRol(v[rolIndex].trim());
        return r;
    }

    private String cell(Row row, int index) {
        Cell c = row.getCell(index);
        return c == null ? "" : new DataFormatter().formatCellValue(c);
    }
}
