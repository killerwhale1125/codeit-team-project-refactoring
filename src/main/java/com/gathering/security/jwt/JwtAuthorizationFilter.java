package com.gathering.security.jwt;


import com.gathering.security.auth.PrincipalDetails;
import com.gathering.user.model.User;
import com.gathering.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// 시큐리티filter중 BasicAuthicationFilter
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게되어있음.
// 만약에 권한, 인증이 필요한 주소가 아니라면 해당 필터 x
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${security.header}")
    private String header;

    @Value("${security.token.prefix}")
    private String tokenPrefix;
    private UserRepository userRepository;
    private JwtTokenValidator jwtTokenValidator;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenValidator jwtTokenValidator) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtHeader = request.getHeader(header);
        System.out.println("jwtHeader :" + jwtHeader);

        // header가 있는지 확인
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader(header).replace(tokenPrefix, "");

        String email = jwtTokenValidator.getUserEmail(jwtToken);
        // 서명이 정상적으로 됨
        if(email != null && jwtTokenValidator.validateToken(jwtToken)) {
            User userEntity = userRepository.findByEmail(email);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            // 정상적인 유저이기에 뒤에 비밀번호 대신 null을 넣어서 강제적으로 객체를 만듬
            // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication을 만들어 준다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request,response);
        }
    }
}
