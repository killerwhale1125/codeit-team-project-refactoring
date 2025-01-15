package com.gathering.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {


    private Long usersId;
    private String userName;
    private String email;
    private String profile;
    private String roles; // USER, ADMIN
    private String token;
    private String password;
    private String refreshToken;

    // Entity -> DTO 변환 메서드
    public static UserResponse fromEntity(UserDomain user) {
        return UserResponse.builder()
                .usersId(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .email(user.getEmail())
                .profile(user.getProfile())
                .roles(user.getRoles())
                .build();
    }

    public static UserResponse fromEntity(UserDomain user, String accessToken) {
        return UserResponse.builder()
                .usersId(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .profile(user.getProfile())
                .roles(user.getRoles())
                .token(accessToken)
                .build();
    }
}
