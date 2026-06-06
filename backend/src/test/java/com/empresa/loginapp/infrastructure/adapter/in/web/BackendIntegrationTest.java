package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.domain.model.Usuario;
import com.empresa.loginapp.domain.model.Sesion;
import com.empresa.loginapp.domain.exception.BusinessException;
import com.empresa.loginapp.domain.port.in.*;
import com.empresa.loginapp.shared.dto.response.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:loginapp_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "jwt.secret=CLAVE_SECRETA_TEST_CLAVE_SECRETA_TEST_CLAVE_SECRETA_TEST_123456789",
        "jwt.expiration=3600000"
})
@AutoConfigureMockMvc
class BackendIntegrationTest {
    private final MockMvc mockMvc;
    @MockBean
    AuthUseCase authUseCase;
    @MockBean
    UsuarioUseCase usuarioUseCase;
    @MockBean
    DashboardUseCase dashboardUseCase;
    @MockBean
    MenuUseCase menuUseCase;
    @MockBean
    SesionUseCase sesionUseCase;

    @Autowired
    BackendIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void loginCorrectoEIncorrecto() throws Exception {
        when(authUseCase.login(any())).thenReturn(AuthResponse.builder().token("token").username("Admin1234").rol("ADMIN").build());
        mockMvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"Admin1234\",\"password\":\"Admin@1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));

        when(authUseCase.login(any())).thenThrow(new BusinessException("Credenciales invalidas"));
        mockMvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"credential\":\"Admin1234\",\"password\":\"bad\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuariosSinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void dashboardConUserDevuelve403() throws Exception {
        mockMvc.perform(get("/api/dashboard/resumen"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void dashboardConAdminFunciona() throws Exception {
        when(dashboardUseCase.resumen()).thenReturn(DashboardResumenResponse.builder().totalUsuarios(2).usuariosActivos(2).build());
        mockMvc.perform(get("/api/dashboard/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsuarios").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearUsuarioYEliminarLogico() throws Exception {
        when(usuarioUseCase.create(any())).thenReturn(Usuario.builder().idUsuario(3L).username("Test1234").email("test@mail.com").activo(true).build());
        mockMvc.perform(post("/api/usuarios").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Test1234\",\"password\":\"Test@1234\",\"nombres\":\"Test\",\"apellidos\":\"User\",\"identificacion\":\"0934567890\",\"email\":\"manual@mail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.username").value("Test1234"));

        mockMvc.perform(delete("/api/usuarios/3").with(csrf()))
                .andExpect(status().isOk());
        verify(usuarioUseCase).delete(3L);
    }

    @Test
    @WithMockUser(username = "Admin1234", roles = "ADMIN")
    void menuYFiltroUsuarios() throws Exception {
        when(menuUseCase.menuByAuthenticatedUser("Admin1234")).thenReturn(List.of(
                MenuResponse.builder().nombre("Dashboard").ruta("/dashboard").icono("layout-dashboard").build(),
                MenuResponse.builder().nombre("Sesiones").ruta("/sesiones").icono("clock").build()
        ));
        when(usuarioUseCase.filter(any())).thenReturn(List.of(Usuario.builder().idUsuario(1L).username("Admin1234").email("admin@mail.com").build()));

        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Dashboard"))
                .andExpect(jsonPath("$[1].nombre").value("Sesiones"))
                .andExpect(jsonPath("$[1].ruta").value("/sesiones"))
                .andExpect(jsonPath("$[1].icono").value("clock"));

        mockMvc.perform(get("/api/usuarios/filter?username=Admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].username").value("Admin1234"));
        verify(usuarioUseCase).filter(argThat(filter -> "Admin".equals(filter.getUsername())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void filtroUsuariosConUserDevuelve403() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?username=Admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void sesionesConAdminFunciona() throws Exception {
        when(sesionUseCase.findAll()).thenReturn(List.of(Sesion.builder().idSesion(1L).idUsuario(1L).activa(false).exitoso(true).mensaje("LOGIN_OK").build()));

        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idSesion").value(1))
                .andExpect(jsonPath("$[0].mensaje").value("LOGIN_OK"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void sesionesConUserDevuelve403() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void mantenimientoConUserDevuelve403() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isForbidden());
    }
}
