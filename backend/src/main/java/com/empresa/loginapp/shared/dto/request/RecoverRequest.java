package com.empresa.loginapp.shared.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecoverRequest {
    @JsonAlias("login")
    @NotBlank
    private String credential;
}
