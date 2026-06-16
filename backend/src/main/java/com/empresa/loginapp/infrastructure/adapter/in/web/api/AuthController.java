package com.empresa.loginapp.infrastructure.adapter.in.web.api;

import com.empresa.loginapp.domain.port.in.AuthUseCase;
import com.empresa.loginapp.infrastructure.security.CookieService;
import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthUseCase authUseCase;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authUseCase.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.createAccessTokenCookie(response.getToken()).toString())
                .body(response.withoutToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication == null ? null : authentication.getName();
        authUseCase.logout(username, cookieService.resolveAccessToken(request).orElse(null));
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.expireAccessTokenCookie().toString());
        return ResponseEntity.ok(Map.of("message", "Logout correcto"));
    }

    @GetMapping("/me")
    public AuthResponse me(Authentication authentication) {
        return authUseCase.me(authentication.getName()).withoutToken();
    }

    @PostMapping("/recover")
    public Map<String, String> recover(@Valid @RequestBody RecoverPasswordRequest request) {
        return Map.of("message", authUseCase.recover(request));
    }
}
