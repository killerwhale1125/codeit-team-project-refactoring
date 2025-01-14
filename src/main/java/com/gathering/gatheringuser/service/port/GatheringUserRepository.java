package com.gathering.gatheringuser.service.port;

import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.gatheringuser.domain.GatheringUserDomain;

import java.util.List;

public interface GatheringUserRepository {
    GatheringUserDomain save(GatheringUserDomain gatheringUserDomain);

    void deleteById(Long gatheringUserId);

    void join(GatheringUserDomain gatheringUserDomain);

    List<GatheringUserDomain> findByGatheringIdAndStatusWithUsers(Long gatheringId, GatheringUserStatus gatheringUserStatus);

    void saveAll(List<GatheringUserDomain> gatheringUsers);
}
