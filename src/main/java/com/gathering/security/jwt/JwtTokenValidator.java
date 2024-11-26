package com.gathering.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import java.security.PublicKey;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenValidator {
    public static final String claimInfo = "preferred_username";

    @Autowired
    private JwtKeyProvider jwtKeyProvider;

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);

            // 토큰 만료
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            return true;

        } catch (Exception e) {
             return false;
        }
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get(claimInfo, String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKeyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
