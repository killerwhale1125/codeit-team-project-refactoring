package com.gathering.gathering.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.service.port.GatheringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_GATHERING;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepository {

    private final GatheringJpaRepository gatheringJpaRepository;

    @Override
    public GatheringDomain save(GatheringDomain gathering) {
        return gatheringJpaRepository.save(Gathering.fromEntity(gathering)).toEntity();
    }

    @Override
    public GatheringDomain getById(Long gatheringId) {
        return gatheringJpaRepository.findById(gatheringId).orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING)).toEntity();
    }

    @Override
    public void delete(Gathering gathering) {
        gatheringJpaRepository.delete(gathering);
    }

    @Override
    public void deleteById(Long gatheringId) {
        gatheringJpaRepository.deleteById(gatheringId);
    }

    @Override
    public GatheringDomain findByIdWithGatheringUsers(long gatheringId) {
        return gatheringJpaRepository
                .findByIdWithGatheringUsers(gatheringId).orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING))
                .toEntity();
    }

    @Override
    public GatheringDomain findByIdWithBookAndChallenge(Long gatheringId) {
        return gatheringJpaRepository.findByIdWithBookAndChallenge(gatheringId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING))
                .toEntity();
    }

    @Override
    public void leave(GatheringDomain gathering) {
        gatheringJpaRepository.updateCurrentCapacityAndStatus(gathering.getId(), gathering.getCurrentCapacity(), gathering.getGatheringStatus());
    }

    @Override
    public GatheringDomain findByIdWithGatheringUsersAndChallenge(Long gatheringId) {
        return gatheringJpaRepository.findByIdWithGatheringUsersAndChallenge(gatheringId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING)).toEntity();
    }

    @Override
    public GatheringDomain getByIdWithGatheringUsersAndChallenge(Long gatheringId) {
        return gatheringJpaRepository.getByIdWithGatheringUsersAndChallenge(gatheringId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING))
                .toEntity();
    }

    @Override
    public List<Gathering> findByIdIn(List<Long> list) {
        return gatheringJpaRepository.findByIdIn(list);
    }

    @Override
    public Long findIdById(Long gatheringId) {
        return gatheringJpaRepository.findIdById(gatheringId).orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public long getActiveAndParticipatingCount(long userId) {
        return gatheringJpaRepository.getActiveAndParticipatingCountByUserId(userId);
    }

    @Override
    public long getCompletedCount(long userId) {
        return gatheringJpaRepository.getCompletedCountByUserId(userId);
    }

    @Override
    public long getMyCreatedCount(String userName) {
        return gatheringJpaRepository.getMyCreatedCountByUserName(userName);
    }

    @Override
    public long getMyWishedCountByGatheringIds(Set<Long> wishGatheringIds) {
        return gatheringJpaRepository.getMyWishedCountByGatheringIds(wishGatheringIds);
    }

    @Override
    public void join(GatheringDomain gathering) {
        gatheringJpaRepository.updateCurrentCapacityAndStatus(gathering.getId(), gathering.getCurrentCapacity(), gathering.getGatheringStatus());
    }

}
