package com.gathering.gathering.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_GATHERING;

@Repository
@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepository {

    private final GatheringJpaRepository gatheringJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Gathering gathering) {
        gatheringJpaRepository.save(gathering);
    }

    @Override
    public Gathering getById(Long gatheringId) {
        return gatheringJpaRepository.findById(gatheringId).orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public void delete(Gathering gathering) {
        gatheringJpaRepository.delete(gathering);
    }

    @Override
    public Gathering findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        return gatheringJpaRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus)
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public Gathering getGatheringAndGatheringUsersById(Long gatheringId) {
        return gatheringJpaRepository.getGatheringAndGatheringUsersById(gatheringId).orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
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

}
