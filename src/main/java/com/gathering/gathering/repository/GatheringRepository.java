package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;

import java.util.List;
import java.util.Set;

public interface GatheringRepository {

    void save(Gathering gathering);

    Gathering getById(Long gatheringId);

    void delete(Gathering gathering);

    Gathering findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringStatus);

    Gathering getGatheringAndGatheringUsersById(Long gatheringId);

    List<Gathering> findByIdIn(List<Long> gatheringIds);

    Long findIdById(Long gatheringId);

    long getActiveAndParticipatingCount(long id);

    long getCompletedCount(long id);

    long getMyCreatedCount(String userName);

    long getMyWishedCountByGatheringIds(Set<Long> wishGatheringIds);
}
