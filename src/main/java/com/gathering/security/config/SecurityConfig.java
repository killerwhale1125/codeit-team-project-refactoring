package com.gathering.security.config;

import com.gathering.security.jwt.JwtAuthorizationFilter;
import com.gathering.security.jwt.JwtTokenValidator;
import com.gathering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured()어노테이션 활성화
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtTokenValidator jwtTokenValidator;
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(this.userDetailsService);
        AuthenticationManager manager = builder.build();
        http.authenticationManager(manager);

        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(corsFilter) // @CrossOrigin(인증 x), 시큐리티 필터에 등록 인증 (O)
                .formLogin(AbstractHttpConfigurer::disable) // 시큐리티 로그인 화면 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilter(new JwtAuthorizationFilter(manager, userRepository, jwtTokenValidator)) // AuthenticationManger
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/v1/user/**").hasAnyRole("USER")
//                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                ).build();
    }
}
