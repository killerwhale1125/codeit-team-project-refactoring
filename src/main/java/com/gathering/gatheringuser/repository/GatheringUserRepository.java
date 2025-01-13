package com.gathering.gatheringuser.repository;

import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;

import java.util.List;

public interface GatheringUserRepository {
    GatheringUserDomain save(GatheringUserDomain gatheringUserDomain);

    void deleteById(Long gatheringUserId);

    void join(GatheringUserDomain gatheringUserDomain);

    List<GatheringUserDomain> findByGatheringIdAndStatusWithUsers(Long gatheringId, GatheringUserStatus gatheringUserStatus);

    void saveAll(List<GatheringUserDomain> gatheringUsers);
}
