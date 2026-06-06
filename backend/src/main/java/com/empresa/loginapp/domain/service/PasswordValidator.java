package com.empresa.loginapp.domain.service;

import com.empresa.loginapp.domain.exception.BusinessException;

public class PasswordValidator {
    private static final String REGEX = "^(?=.*[A-Z])(?=.*[^A-Za-z0-9])\\S{8,}$";

    public void validate(String password) {
        if (password == null || !password.matches(REGEX)) {
            throw new BusinessException("Password invalido: minimo 8 caracteres, una mayuscula, un signo y sin espacios");
        }
    }
}
