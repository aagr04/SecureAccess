package com.empresa.loginapp.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenSessionService {
    private static final String BLACKLIST_PREFIX = "logout:token:";

    private final RedisTemplate<String, String> redisTemplate;

    public void invalidate(String jti, Duration ttl) {
        if (jti == null || jti.isBlank() || ttl == null || ttl.isZero() || ttl.isNegative()) {
            return;
        }
        String key = key(jti);
        try {
            redisTemplate.opsForValue().set(key, "logout", ttl);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis no disponible. No se puede invalidar el token de forma distribuida.", ex);
            throw new IllegalStateException("No se pudo invalidar la sesion en Redis", ex);
        }
    }

    public boolean isInvalidated(String jti) {
        if (jti == null || jti.isBlank()) {
            return true;
        }
        String key = key(jti);
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis no disponible. Se rechaza la sesion por seguridad.", ex);
            return true;
        }
    }

    private String key(String jti) {
        return BLACKLIST_PREFIX + jti;
    }
}
