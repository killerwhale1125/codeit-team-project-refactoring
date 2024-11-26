package com.gathering.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import java.util.Date;

@Component
public class JwtTokenValidator {

    private String secretKey;


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

    public String getUserEmail(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject(); // jwt 구성중 sub 클레임
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
