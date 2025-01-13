package com.gathering.gathering.repository;

import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;

import java.util.List;
import java.util.Set;

public interface GatheringRepository {

    GatheringDomain save(GatheringDomain gathering);

    GatheringDomain getById(Long gatheringId);

    void delete(Gathering gathering);

    GatheringDomain findByIdWithGatheringUsersAndChallenge(Long gatheringId);

    GatheringDomain getByIdWithGatheringUsersAndChallenge(Long gatheringId);

    List<Gathering> findByIdIn(List<Long> gatheringIds);

    Long findIdById(Long gatheringId);

    long getActiveAndParticipatingCount(long userId);

    long getCompletedCount(long userId);

    long getMyCreatedCount(String userName);

    long getMyWishedCountByGatheringIds(Set<Long> wishGatheringIds);

    void join(GatheringDomain gathering);

    void deleteById(Long gatheringId);

    GatheringDomain findByIdWithGatheringUsers(long gatheringId);

    GatheringDomain findByIdWithBookAndChallenge(Long gatheringId);

    void leave(GatheringDomain gathering);
}
