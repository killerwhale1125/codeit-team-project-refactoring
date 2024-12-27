package com.gathering.user.model.dto;

import com.gathering.user.model.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserDto {


    private long usersId;
    private String userName;
    private String email;
    private String profile;
    private String roles; // USER, ADMIN
    private String token;
    private String password;

    private String refreshToken;

    // Entity -> DTO 변환 메서드
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .usersId(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .email(user.getEmail())
                .profile(user.getProfile())
                .roles(user.getRoles())
                .build();
    }

    public UserDto(long usersId, String userName, String email, String profile, String roles) {
        this.usersId = usersId;
        this.userName = userName;
        this.email = email;
        this.profile = profile;
        this.roles = roles;
    }
}
