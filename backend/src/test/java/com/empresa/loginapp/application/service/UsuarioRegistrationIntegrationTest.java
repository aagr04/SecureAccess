package com.empresa.loginapp.application.service;

import com.empresa.loginapp.domain.exception.BusinessException;
import com.empresa.loginapp.domain.model.Rol;
import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.domain.port.in.AuthUseCase;
import com.empresa.loginapp.domain.port.in.UsuarioUseCase;
import com.empresa.loginapp.domain.port.out.MenuFunctionPort;
import com.empresa.loginapp.domain.port.out.RolRepositoryPort;
import com.empresa.loginapp.shared.dto.request.LoginRequest;
import com.empresa.loginapp.shared.dto.request.UsuarioRequest;
import com.empresa.loginapp.shared.dto.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:registration_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "jwt.secret=CLAVE_SECRETA_TEST_CLAVE_SECRETA_TEST_CLAVE_SECRETA_TEST_123456789",
        "jwt.expiration=3600000"
})
class UsuarioRegistrationIntegrationTest {
    @Autowired
    UsuarioUseCase usuarios;
    @Autowired
    AuthUseCase auth;
    @Autowired
    RolRepositoryPort roles;
    @MockBean
    MenuFunctionPort menuFunction;

    @Test
    void registroGeneraEmailYLoginFuncionaConEmailOUsername() {
        when(menuFunction.menuPorRol(anyLong())).thenReturn(List.of());
        Rol rol = roles.save(Rol.builder().nombre("USER").activo(true).build());

        Usuario creado = usuarios.create(request("Juan2024A", "1203574901", "Juan Alberto", "Piguave Loor", null, rol.getIdRol()));
        assertThat(creado.getEmail()).isEqualTo("jpiguavel@mail.com");

        UsuarioRequest requestConRolNombre = request("Maria2024A", "1203574902", "Juan Alberto", "Piguave Loor", "manual@mail.com", null);
        requestConRolNombre.setRol("USER");
        Usuario conEmailManual = usuarios.create(requestConRolNombre);
        assertThat(conEmailManual.getEmail()).isEqualTo("jpiguavel1@mail.com");

        assertThatThrownBy(() -> usuarios.create(request("Juan2024B", "1203574901", "Juan Alberto", "Piguave Loor", null, rol.getIdRol())))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cuenta registrada");
        assertThatThrownBy(() -> usuarios.create(request("Juan2024A", "1203574903", "Juan Alberto", "Piguave Loor", null, rol.getIdRol())))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("nombre de usuario");
        assertThatThrownBy(() -> usuarios.create(request("juan2024a", "1203574904", "Juan Alberto", "Piguave Loor", null, rol.getIdRol())))
                .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> usuarios.create(request("Pedro2024A", "1203574905", "Juan Alberto", "Piguave Loor", null, rol.getIdRol(), "clave123")))
                .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> usuarios.create(request("Pedro2024B", "1203555577", "Juan Alberto", "Piguave Loor", null, rol.getIdRol())))
                .isInstanceOf(BusinessException.class);

        AuthResponse loginEmail = auth.login(login("jpiguavel@mail.com"));
        assertThat(loginEmail.getToken()).isNotBlank();
        assertThat(loginEmail.getEmail()).isEqualTo("jpiguavel@mail.com");
        AuthResponse loginUsername = auth.login(login("Juan2024A"));
        assertThat(loginUsername.getToken()).isNotBlank();
        assertThat(loginUsername.getUsername()).isEqualTo("Juan2024A");

        auth.logout("Juan2024A");
    }

    private UsuarioRequest request(String username, String identificacion, String nombres, String apellidos, String email, Long idRol) {
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername(username);
        request.setPassword("Clave@123");
        request.setNombres(nombres);
        request.setApellidos(apellidos);
        request.setIdentificacion(identificacion);
        request.setEmail(email);
        request.setStatus("ACTIVO");
        request.setIdRol(idRol);
        return request;
    }

    private UsuarioRequest request(String username, String identificacion, String nombres, String apellidos, String email, Long idRol, String password) {
        UsuarioRequest request = request(username, identificacion, nombres, apellidos, email, idRol);
        request.setPassword(password);
        return request;
    }

    private LoginRequest login(String credential) {
        LoginRequest request = new LoginRequest();
        request.setCredential(credential);
        request.setPassword("Clave@123");
        return request;
    }
}
