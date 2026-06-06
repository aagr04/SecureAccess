package com.empresa.loginapp.infrastructure.adapter.out.persistence.criteria;

import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.PersonaEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.RolUsuarioEntity;
import com.empresa.loginapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.empresa.loginapp.shared.dto.request.UsuarioFilterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UsuarioCriteriaRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:usuarios_criteria_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class UsuarioCriteriaRepositoryTest {
    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Autowired
    private UsuarioCriteriaRepository repository;

    @BeforeEach
    void setup() {
        RolEntity admin = persistRole("ADMIN");
        RolEntity user = persistRole("USER");

        UsuarioEntity agustin = persistUser("Agustin1999", "aguevarar@mail.com", "ACTIVO", true,
                "Agustin", "Guevara Reyes", "1203574901");
        UsuarioEntity juan = persistUser("Juan2024A", "jpiguavel@mail.com", "ACTIVO", true,
                "Juan Alberto", "Piguave Loor", "0912345678");
        UsuarioEntity ana = persistUser("Ana2024B", "ana@mail.com", "BLOQUEADO", true,
                "Ana", "Paz", "0912345679");
        persistUser("Inactivo2024A", "inactivo@mail.com", "ACTIVO", false,
                "Inactivo", "Oculto", "0912345680");

        persistRoleUser(agustin, admin);
        persistRoleUser(juan, user);
        persistRoleUser(ana, user);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void buscaSoloPorIdentificacionExacta() {
        UsuarioFilterRequest filter = new UsuarioFilterRequest();
        filter.setIdentificacion("1203574901");

        assertThat(usernames(repository.filter(filter))).containsExactly("Agustin1999");

        filter.setIdentificacion("120357490");
        assertThat(repository.filter(filter)).isEmpty();
    }

    @Test
    void buscaSoloPorUsernameParcialEIgnoraMayusculas() {
        UsuarioFilterRequest filter = new UsuarioFilterRequest();
        filter.setUsername("agustin");

        assertThat(usernames(repository.filter(filter))).containsExactly("Agustin1999");
    }

    @Test
    void buscaSoloPorEmailParcialEIgnoraMayusculas() {
        UsuarioFilterRequest filter = new UsuarioFilterRequest();
        filter.setEmail("GUEVARAR@MAIL");

        assertThat(usernames(repository.filter(filter))).containsExactly("Agustin1999");
    }

    @Test
    void buscaPorNombresApellidosYCombinados() {
        UsuarioFilterRequest nombres = new UsuarioFilterRequest();
        nombres.setNombres("juan");
        assertThat(usernames(repository.filter(nombres))).containsExactly("Juan2024A");

        UsuarioFilterRequest apellidos = new UsuarioFilterRequest();
        apellidos.setApellidos("guevara");
        assertThat(usernames(repository.filter(apellidos))).containsExactly("Agustin1999");

        UsuarioFilterRequest combinados = new UsuarioFilterRequest();
        combinados.setNombres("Agustin");
        combinados.setApellidos("Reyes");
        assertThat(usernames(repository.filter(combinados))).containsExactly("Agustin1999");
    }

    @Test
    void buscaPorEstadoRolYCombinacion() {
        UsuarioFilterRequest estado = new UsuarioFilterRequest();
        estado.setStatus("BLOQUEADO");
        assertThat(usernames(repository.filter(estado))).containsExactly("Ana2024B");

        UsuarioFilterRequest rol = new UsuarioFilterRequest();
        rol.setRol("USER");
        assertThat(usernames(repository.filter(rol))).containsExactly("Juan2024A", "Ana2024B");

        UsuarioFilterRequest estadoRol = new UsuarioFilterRequest();
        estadoRol.setStatus("ACTIVO");
        estadoRol.setRol("USER");
        assertThat(usernames(repository.filter(estadoRol))).containsExactly("Juan2024A");
    }

    @Test
    void sinFiltrosOConParametrosVaciosListaActivosYSinCoincidenciasRetornaVacio() {
        assertThat(usernames(repository.filter(new UsuarioFilterRequest()))).containsExactly("Agustin1999", "Juan2024A", "Ana2024B");

        UsuarioFilterRequest vacios = new UsuarioFilterRequest();
        vacios.setNombres(" ");
        vacios.setUsername("");
        vacios.setEmail("   ");
        assertThat(usernames(repository.filter(vacios))).containsExactly("Agustin1999", "Juan2024A", "Ana2024B");

        UsuarioFilterRequest inexistente = new UsuarioFilterRequest();
        inexistente.setUsername("NoExiste");
        assertThat(repository.filter(inexistente)).isEmpty();
    }

    private RolEntity persistRole(String nombre) {
        RolEntity rol = RolEntity.builder().nombre(nombre).activo(true).build();
        entityManager.persist(rol);
        return rol;
    }

    private UsuarioEntity persistUser(String username, String email, String status, boolean activo, String nombres, String apellidos, String identificacion) {
        PersonaEntity persona = PersonaEntity.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .identificacion(identificacion)
                .activo(true)
                .build();
        entityManager.persist(persona);

        UsuarioEntity usuario = UsuarioEntity.builder()
                .username(username)
                .password("hash")
                .email(email)
                .status(status)
                .activo(activo)
                .intentosFallidos(0)
                .sesionActiva(false)
                .persona(persona)
                .build();
        entityManager.persist(usuario);
        return usuario;
    }

    private void persistRoleUser(UsuarioEntity usuario, RolEntity rol) {
        entityManager.persist(RolUsuarioEntity.builder().usuario(usuario).rol(rol).activo(true).build());
    }

    private List<String> usernames(List<UsuarioEntity> usuarios) {
        return usuarios.stream().map(UsuarioEntity::getUsername).toList();
    }
}
