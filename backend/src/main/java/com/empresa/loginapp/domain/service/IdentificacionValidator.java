package com.empresa.loginapp.domain.service;

import com.empresa.loginapp.domain.exception.BusinessException;

public class IdentificacionValidator {
    public void validate(String identificacion) {
        if (identificacion == null || !identificacion.matches("^\\d{10}$") || identificacion.matches(".*(\\d)\\1\\1\\1.*")) {
            throw new BusinessException("Identificacion invalida: debe tener 10 digitos y no puede contener 4 numeros iguales seguidos");
        }
    }
}
