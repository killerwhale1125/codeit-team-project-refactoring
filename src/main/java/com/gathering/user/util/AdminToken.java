package com.gathering.user.util;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
// 미사용으로 주석처리
//@Component
public class AdminToken {

//    @Value("${keycloak.admin.accessTokenPath}")
//    private String path;
//
//    @Value("${keycloak.admin.addUser}")
//    private String userPath;
//
//    @Value("${keycloak.admin.id}")
//    private String adminId;
//
//    @Value("${keycloak.admin.password}")
//    private String adminPassword;
//
//    @Value("${keycloak.admin.clientId}")
//    private String clientId;
//
//    @Value("${keycloak.grantType}")
//    private String grantType;
//    @Resource
//    private RestTemplate restTemplate;
//
//
//
//    /**
//     * 사용자 추가를 위해 admin 토큰 발행
//     */
//    public String getAdminAccessToken() {
//        // Headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//
//        // Body
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", grantType);
//        body.add("client_id", clientId);
//        body.add("username", adminId);
//        body.add("password", adminPassword);
//
//        // HTTP 요청 생성
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//        // 요청 보내기
//        ResponseEntity<Map> response = restTemplate.exchange(
//                path,
//                HttpMethod.POST,
//                request,
//                Map.class
//        );
//        // 토큰 추출
//        Map<String, Object> responseBody = response.getBody();
//
//        return (String) responseBody.get("access_token");
//    }
//
//    /**
//     * 사용자 추가
//     */
//
//    public boolean addUserKeyCloak(SignUpRequestDto signUpRequestDto, String token) {
//        // 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + token); // Admin Token 추가
//        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//
//        // 사용자 생성 요청 데이터
//        Map<String, Object> userPayload = new HashMap<>();
//        userPayload.put("username", signUpRequestDto.getUserName());
//        userPayload.put("lastName", "codeit");
//        userPayload.put("firstName", "codeit");
//        userPayload.put("email", signUpRequestDto.getEmail());
//        userPayload.put("enabled", true);
//
//        Map<String, Object> credentials = new HashMap<>();
//        credentials.put("type", "password");
//        credentials.put("value", signUpRequestDto.getPassword());
//        credentials.put("temporary", false);
//
//        userPayload.put("credentials", new Map[]{credentials});
//        // HTTP 요청 생성
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
//
//
//        // 요청 보내기
//        ResponseEntity<String> response = restTemplate.exchange(
//                userPath,
//                HttpMethod.POST,
//                request,
//                String.class
//        );
//
//        return response.getStatusCode().is2xxSuccessful();
//    }
}
