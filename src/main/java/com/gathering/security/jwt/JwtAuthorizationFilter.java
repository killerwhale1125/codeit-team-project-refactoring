package com.gathering.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.security.auth.PrincipalDetails;
import com.gathering.user.infrastructure.entitiy.User;
import com.gathering.user.infrastructure.UserJpaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.NOT_EXISTED_USER;
import static com.gathering.security.jwt.JwtTokenUtil.extractUsername;
import static com.gathering.security.jwt.JwtTokenUtil.validateToken;

/*
* JWT 검증 필터
* */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String header = "Authorization";
    public static final String tokenPrefix = "Bearer ";
    private UserJpaRepository userJpaRepository;
    private List<String> excludePaths;
    private List<String> publicPaths;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserJpaRepository userJpaRepository,
                                  List<String> excludePaths, List<String> publicPaths) {
        super(authenticationManager);
        this.userJpaRepository = userJpaRepository;
        this.excludePaths = excludePaths;
        this.publicPaths = publicPaths;
    }

    // 토큰 관련 오류 발생시 응답
    public void FailedAuthorizationToken(HttpServletResponse response, BaseResponseStatus status) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        BaseResponse<String> errorResponse = new BaseResponse<>(status);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }

    public void FailedAuthorization(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        BaseResponse<String> errorResponse = new BaseResponse<>(BaseResponseStatus.UNKNOWN_ERROR);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }


    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtHeader = request.getHeader(header);
        // 요청 경로가 제외된 경로 리스트에 포함되면 필터를 거치지 않음
        String requestUri = request.getRequestURI();
        // 경로가 제외 목록에 있는지 확인
//        boolean isExcluded = excludePaths.stream()
//                .anyMatch(requestUri::equals);

        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isExcluded = excludePaths.stream()
                .anyMatch(path -> pathMatcher.match(path, requestUri));

        if(isExcluded || !requestUri.startsWith("/api") || requestUri.startsWith("/api/auths/check")) {
            chain.doFilter(request, response);
            return;
        } else {
            boolean isPublic = publicPaths.stream()
                    .anyMatch(path -> pathMatcher.match(path, requestUri));

            if(isPublic && (jwtHeader == null || !jwtHeader.startsWith("Bearer"))) {
                chain.doFilter(request, response);
                return;
            } else if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
                // 401 Unauthorized 응답 처리
                FailedAuthorization(response);
                return;
            }


            // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
            try {
                String jwtToken = request.getHeader(header).replace(tokenPrefix, "");
                String userName = null;
                try {
                    userName = extractUsername(jwtToken);
                } catch (io.jsonwebtoken.SignatureException se) {
                    FailedAuthorizationToken(response, BaseResponseStatus.UNSUPPORTED_TOKEN);
                    return;
                }
                // 서명이 정상적으로 됨
                if(userName != null && validateToken(jwtToken)) {
                    User userEntity = userJpaRepository.findByUserName(userName).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
                    PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                    // 정상적인 유저이기에 뒤에 비밀번호 대신 null을 넣어서 강제적으로 객체를 만듬
                    // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication을 만들어 준다.
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                    // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    chain.doFilter(request,response);
                } else {
                    FailedAuthorizationToken(response, BaseResponseStatus.EXPIRED_TOKEN);
                }
            } catch (Exception e) {
                e.printStackTrace();
                FailedAuthorization(response);
            }

        }
    }
}
