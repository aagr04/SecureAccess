package com.empresa.loginapp.infrastructure.security;

import com.empresa.loginapp.domain.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import javax.crypto.SecretKey;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Usuario usuario, String rol) {
        return Jwts.builder().subject(usuario.getUsername())
                .claim("idUsuario", usuario.getIdUsuario()).claim("rol", rol)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key()).compact();
    }

    public String extractUsername(String token) {
        return claims(token).getSubject();
    }

    public String extractJti(String token) {
        return claims(token).getId();
    }

    public Duration remainingTtl(String token) {
        long remaining = claims(token).getExpiration().getTime() - System.currentTimeMillis();
        return Duration.ofMillis(Math.max(remaining, 0));
    }

    public boolean isValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && claims(token).getExpiration().after(new Date());
    }

    private Claims claims(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
