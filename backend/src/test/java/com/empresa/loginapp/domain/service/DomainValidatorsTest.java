package com.empresa.loginapp.domain.service;

import com.empresa.loginapp.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.assertj.core.api.Assertions.*;

class DomainValidatorsTest {
    @Test
    void usernameValidoEInvalido() {
        UsernameValidator validator = new UsernameValidator();
        assertThatCode(() -> validator.validate("Admin1234")).doesNotThrowAnyException();
        assertThatThrownBy(() -> validator.validate("admin@123")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("admin1234")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("AdminUser")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("Adm123")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("AdminUsuario123456789")).isInstanceOf(BusinessException.class);
    }
    @Test
    void passwordValidoEInvalido() {
        PasswordValidator validator = new PasswordValidator();
        assertThatCode(() -> validator.validate("Admin@1234")).doesNotThrowAnyException();
        assertThatThrownBy(() -> validator.validate("Admin 1234")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("Abc@12")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("clave@123")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("Clave123")).isInstanceOf(BusinessException.class);
    }
    @Test
    void identificacionValidaEInvalida() {
        IdentificacionValidator validator = new IdentificacionValidator();
        assertThatCode(() -> validator.validate("0912345678")).doesNotThrowAnyException();
        assertThatThrownBy(() -> validator.validate("0911115678")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("123456789")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("12345678901")).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> validator.validate("12345A7890")).isInstanceOf(BusinessException.class);
    }
    @Test
    void emailAutomaticoBase() {
        EmailGeneratorService service = new EmailGeneratorService();
        assertThat(service.generate("Juan Alberto", "Piguave Loor", Set.of())).isEqualTo("jpiguavel@mail.com");
    }

    @Test
    void emailAutomaticoConUnDuplicado() {
        EmailGeneratorService service = new EmailGeneratorService();
        assertThat(service.generate("Juan Alberto", "Piguave Loor", Set.of("jpiguavel@mail.com"))).isEqualTo("jpiguavel1@mail.com");
    }

    @Test
    void emailAutomaticoConDosDuplicados() {
        EmailGeneratorService service = new EmailGeneratorService();
        assertThat(service.generate("Juan Alberto", "Piguave Loor", Set.of("jpiguavel@mail.com", "jpiguavel1@mail.com")))
                .isEqualTo("jpiguavel2@mail.com");
    }

    @Test
    void emailAutomaticoLimpiaEspaciosYMayusculas() {
        EmailGeneratorService service = new EmailGeneratorService();
        assertThat(service.generate("  JUAN   ALBERTO  ", "  PIGUAVE   LOOR  ", Set.of())).isEqualTo("jpiguavel@mail.com");
    }

    @Test
    void emailAutomaticoQuitaTildes() {
        EmailGeneratorService service = new EmailGeneratorService();
        assertThat(service.generate("Jose Alberto", "Alava Lopez", Set.of())).isEqualTo("jalaval@mail.com");
        assertThat(service.generate("José Alberto", "Álava López", Set.of())).isEqualTo("jalaval@mail.com");
    }

    @Test
    void emailAutomaticoConUnApellido() {
        EmailGeneratorService service = new EmailGeneratorService();
        assertThat(service.generate("Juan Alberto", "Piguave", Set.of())).isEqualTo("jpiguave@mail.com");
    }
}
