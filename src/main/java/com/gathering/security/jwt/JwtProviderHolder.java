package com.gathering.security.jwt;

import java.util.Optional;

public interface JwtProviderHolder {
    String createAccessToken(String username);

    String saveRefreshToken(String userName);

    void deleteRefreshToken(String username);

    String extractUsername(String refreshToken);

    Optional<String> getRefreshToken(String username);

    String resolveToken(String bearerToken);

    boolean validateToken(String jwtToken);
}
