package com.gathering.gathering_user.util;

import com.gathering.gathering_user.domain.GatheringUserDomain;
import com.gathering.user.controller.response.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class GatheringUserDtoMapper {
    public static List<UserResponseDto> mapToUserResponseDtos(List<GatheringUserDomain> gatheringUsers) {
        return gatheringUsers.stream()
                .map(gatheringUser -> UserResponseDto.fromEntity(gatheringUser.getUser()))
                .collect(Collectors.toList());
    }
}
