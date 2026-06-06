package com.empresa.loginapp.domain.service;

import com.empresa.loginapp.domain.exception.BusinessException;

public class UsernameValidator {
    private static final String REGEX = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$";

    public void validate(String username) {
        if (username == null || !username.matches(REGEX)) {
            throw new BusinessException("Username invalido: debe tener 8 a 20 caracteres, una mayuscula, un numero y no contener signos");
        }
    }
}
