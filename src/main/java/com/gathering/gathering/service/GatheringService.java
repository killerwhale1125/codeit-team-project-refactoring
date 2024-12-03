package com.gathering.gathering.service;

import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.security.auth.PrincipalDetails;
import com.gathering.user.model.dto.response.UserResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface GatheringService {
    void create(GatheringCreate gatheringCreate, String username);

    GatheringResponse getGatheringByGatheringId(Long gatheringId);

    void join(Long gatheringId, String username);

    void delete(Long gatheringId, String username);

    List<UserResponseDto> findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringStatus);

    void leave(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus);

//    void incrementViewCountAsync(Long gatheringId);
}
