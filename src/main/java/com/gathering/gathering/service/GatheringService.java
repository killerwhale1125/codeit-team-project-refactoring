package com.gathering.gathering.service;

import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.user.model.dto.response.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface GatheringService {
    void create(GatheringCreate gatheringCreate, UserDetails userDetails);

    GatheringResponse getGatheringByGatheringId(Long gatheringId);

    void join(Long gatheringId, String username);

    void delete(Long gatheringId, String username);

    List<UserResponseDto> findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringStatus);
}
