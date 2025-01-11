package com.gathering.user.model.dto.response;

import com.gathering.user.model.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private long usersId;
    private String userName;
    private String email;
    private String profile;
    private String roles; // USER, ADMIN

    public static UserResponseDto fromEntity(UserDomain user) {
        return UserResponseDto.builder()
                .usersId(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .profile(user.getProfile())
                .roles(user.getRoles())
                .build();
    }
}
