package com.empresa.loginapp.infrastructure.adapter.in.web;

import com.empresa.loginapp.domain.port.in.AuthUseCase;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        return authUseCase.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Principal principal) {
        authUseCase.logout(principal.getName()); return ResponseEntity.noContent().build();
    }

    @PostMapping("/recover")
    public Map<String, String> recover(@Valid @RequestBody RecoverPasswordRequest request) {
        return Map.of("message", authUseCase.recover(request));
    }
}
