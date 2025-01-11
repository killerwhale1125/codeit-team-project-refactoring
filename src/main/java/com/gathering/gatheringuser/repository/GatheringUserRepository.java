package com.gathering.gatheringuser.repository;

import com.gathering.gatheringuser.model.domain.GatheringUserDomain;

public interface GatheringUserRepository {
    GatheringUserDomain save(GatheringUserDomain gatheringUserDomain);

    void deleteById(Long gatheringUserId);

    void join(GatheringUserDomain gatheringUserDomain);

}
