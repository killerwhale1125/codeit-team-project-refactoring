package com.gathering.security.config;

import com.gathering.security.jwt.JwtAuthorizationFilter;
import com.gathering.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserDetailsService userDetailsService;
    private final UserJpaRepository userJpaRepository;
    @Value("${security.exclude.paths}")
    private List<String> excludePaths;
    @Value("${security.public.paths}")
    private List<String> publicPaths;

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
                .addFilter(new JwtAuthorizationFilter(manager, userJpaRepository, excludePaths, publicPaths)) // AuthenticationManger
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                // 관리자 기능 구현 후 권한 관련 기능 활성화 필요
//                                .requestMatchers("/api/register/**").permitAll()
//                                .requestMatchers("/api/**").hasAnyRole("USER")
//                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
