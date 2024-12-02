package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.gathering.model.entity.GatheringUserStatus;

import java.util.List;

public interface GatheringRepository {

    void save(Gathering gathering);

    Gathering getGatheringByGatheringId(Long gatheringId);

    Gathering getById(Long gatheringId);

    void delete(Gathering gathering);

    Gathering findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringStatus);
}
