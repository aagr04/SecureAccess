package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.BulkUploadResponse;
import java.util.List;

public interface UsuarioUseCase {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario findByUsername(String username);

    Usuario create(UsuarioRequest request);

    Usuario update(String authenticatedUsername, Long id, UsuarioRequest request);

    Usuario updateOwnProfile(String username, UsuarioRequest request);

    void delete(String authenticatedUsername, Long id);

    Usuario changeEstado(String authenticatedUsername, Long id, EstadoUsuarioRequest request);

    List<Usuario> filter(UsuarioFilterRequest filter);

    BulkUploadResponse bulkUpload(BulkUploadRequest file);
}
