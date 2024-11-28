package com.gathering.user.model.dto.response;

import com.gathering.user.model.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private long usersId;
    private String userName;
    private String email;
    private String profile;
    private String roles; // USER, ADMIN

    // Entity -> DTO 변환 메서드
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .usersId(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .profile(user.getProfile())
                .roles(user.getRoles())
                .build();
    }
}
