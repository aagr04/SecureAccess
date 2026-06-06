package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.PersonaEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolUsuarioEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.RolJpaRepository;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.RolUsuarioJpaRepository;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:usuario_filter_endpoint_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.open-in-view=false",
        "jwt.secret=CLAVE_SECRETA_TEST_CLAVE_SECRETA_TEST_CLAVE_SECRETA_TEST_123456789",
        "jwt.expiration=3600000"
})
@AutoConfigureMockMvc
class UsuarioFilterEndpointIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UsuarioJpaRepository usuarioRepository;

    @Autowired
    private RolJpaRepository rolRepository;

    @Autowired
    private RolUsuarioJpaRepository rolUsuarioRepository;

    @BeforeEach
    void setup() {
        rolUsuarioRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
        transactionTemplate.executeWithoutResult(status -> {
            RolEntity userRole = RolEntity.builder().nombre("USER").activo(true).build();
            entityManager.persist(userRole);
            PersonaEntity persona = PersonaEntity.builder()
                    .nombres("Agustin")
                    .apellidos("Guevara Reyes")
                    .identificacion("0923701072")
                    .activo(true)
                    .build();
            entityManager.persist(persona);
            UsuarioEntity usuario = UsuarioEntity.builder()
                    .username("Agustin1999")
                    .password("hash")
                    .email("aguevarar@mail.com")
                    .status("ACTIVO")
                    .activo(true)
                    .intentosFallidos(0)
                    .sesionActiva(false)
                    .persona(persona)
                    .build();
            entityManager.persist(usuario);
            entityManager.persist(RolUsuarioEntity.builder().usuario(usuario).rol(userRole).activo(true).build());
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filtraSoloPorIdentificacionSinError500() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?identificacion=0923701072"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].username").value("Agustin1999"))
                .andExpect(jsonPath("$[0].persona.identificacion").value("0923701072"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filtraIdentificacionInexistenteConListaVacia() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?identificacion=0000000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filtraSoloPorUsernameYEmail() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?username=Agustin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Agustin1999"));

        mockMvc.perform(get("/api/usuarios/filter?email=aguevarar@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("aguevarar@mail.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ignoraEstadoSeleccioneYSinParametrosListaActivos() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?status=Seleccione"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Agustin1999"));

        mockMvc.perform(get("/api/usuarios/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Agustin1999"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNoPuedeFiltrarGlobalmente() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?identificacion=0923701072"))
                .andExpect(status().isForbidden());
    }

    @Test
    void sinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/usuarios/filter?identificacion=0923701072"))
                .andExpect(status().isUnauthorized());
    }
}
