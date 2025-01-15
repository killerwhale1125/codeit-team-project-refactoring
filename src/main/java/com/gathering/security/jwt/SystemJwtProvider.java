package com.gathering.security.jwt;

import com.gathering.common.base.exception.BaseException;
import com.gathering.util.date.DateHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.gathering.common.base.response.BaseResponseStatus.REFRESH_TOKEN_ISEMPTY;

@Component
public class SystemJwtProvider implements JwtProviderHolder {

    private final String SECRET_KEY;
    private final long ACCESS_EXPIRATION_TIME;
    private final long REFRESH_EXPIRATION_TIME;
    private final JwtRedisTemplate jwtRedisTemplate;
    private final DateHolder dateHolder;

    public SystemJwtProvider(@Value("${security.jwt.secret-key}") String SECRET_KEY,
                            @Value("${security.jwt.token.access-expiration-time}") long ACCESS_EXPIRATION_TIME,
                            @Value("${security.jwt.token.refresh-expiration-time}") long REFRESH_EXPIRATION_TIME,
                             JwtRedisTemplate jwtRedisTemplate,
                             DateHolder dateHolder) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_EXPIRATION_TIME = ACCESS_EXPIRATION_TIME;
        this.REFRESH_EXPIRATION_TIME = REFRESH_EXPIRATION_TIME;
        this.jwtRedisTemplate = jwtRedisTemplate;
        this.dateHolder = dateHolder;
    }


    public String createAccessToken(String username) {
        Date now = dateHolder.createDate();
        Date expireDate = dateHolder.createExpireDate(now.getTime(), ACCESS_EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username) // 사용자 이름 설정
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expireDate) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 서명 알고리즘 및 키 설정
                .compact();
    }

    private String createRefreshToken(String username) {
        Date now = dateHolder.createDate();
        Date expireDate = dateHolder.createExpireDate(now.getTime(), REFRESH_EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate) // 7일
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Override
    public String saveRefreshToken(String username) {
        String refreshToken = createRefreshToken(username);

        if(Boolean.TRUE.equals(jwtRedisTemplate.hasKey(refreshToken))) {
            jwtRedisTemplate.deleteRefreshToken(refreshToken);
        }

        // redis에 14일동안 리프레시 토큰 저장
        jwtRedisTemplate.saveRefreshToken(username, refreshToken, 14, TimeUnit.DAYS);
        return refreshToken;
    }

    @Override
    public void deleteRefreshToken(String username) {
        jwtRedisTemplate.deleteRefreshToken(username);
    }

    @Override
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new BaseException(REFRESH_TOKEN_ISEMPTY);
        }
    }

    @Override
    public Optional<String> getRefreshToken(String username) {
        return jwtRedisTemplate.getRefreshToken(username);
    }

    @Override
    public String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //retrieve expiration date from jwt token(토큰으로부터 유효기간 가져오기)
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key(토큰에 저장된 모든정보 가져오기)
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

}
