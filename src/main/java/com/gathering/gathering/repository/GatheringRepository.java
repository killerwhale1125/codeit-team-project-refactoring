package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;

public interface GatheringRepository {

    void save(Gathering gathering);

    Gathering getById(Long gatheringId);

    void delete(Gathering gathering);

    Gathering findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringStatus);

    Gathering getGatheringAndGatheringUsersById(Long gatheringId);
}
