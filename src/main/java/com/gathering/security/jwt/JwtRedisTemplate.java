package com.gathering.security.jwt;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface JwtRedisTemplate {
    Boolean hasKey(String refreshToken);

    void deleteRefreshToken(String refreshToken);

    void saveRefreshToken(String username, String refreshToken, int day, TimeUnit days);

    Optional<String> getRefreshToken(String username);
}
