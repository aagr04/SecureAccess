package com.empresa.loginapp.infrastructure.config;

import com.empresa.loginapp.domain.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    UsernameValidator usernameValidator() {

        return new UsernameValidator();
    }

    @Bean
    PasswordValidator passwordValidator() {

        return new PasswordValidator();
    }

    @Bean
    IdentificacionValidator identificacionValidator() {

        return new IdentificacionValidator();
    }

    @Bean
    EmailGeneratorService emailGeneratorService() {

        return new EmailGeneratorService();
    }
}
