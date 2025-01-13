package com.gathering.mock.fake.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.GatheringRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_GATHERING;
import static com.gathering.gathering.model.entity.GatheringStatus.COMPLETED;
import static com.gathering.gathering.model.entity.GatheringStatus.DELETED;
import static com.gathering.gathering.model.entity.GatheringUserStatus.NOT_PARTICIPATING;
import static com.gathering.gathering.model.entity.GatheringUserStatus.PARTICIPATING;

public class FakeGatheringRepository implements GatheringRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private List<GatheringDomain> data = new ArrayList<>();

    @Override
    public GatheringDomain save(GatheringDomain gathering) {
        if(Objects.isNull(gathering.getId())) {
            GatheringDomain createGathering = GatheringDomain.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .name(gathering.getName())
                    .content(gathering.getContent())
                    .startDate(gathering.getStartDate())
                    .endDate(gathering.getEndDate())
                    .minCapacity(gathering.getMinCapacity())
                    .maxCapacity(gathering.getMaxCapacity())
                    .currentCapacity(gathering.getCurrentCapacity())
                    .owner(gathering.getOwner())
                    .viewCount(gathering.getViewCount())
                    .gatheringStatus(gathering.getGatheringStatus())
                    .gatheringWeek(gathering.getGatheringWeek())
                    .book(gathering.getBook())
                    .challenge(gathering.getChallenge())
                    .image(gathering.getImage())
                    .gatheringUsers(new ArrayList<>())
                    .build();
            data.add(createGathering);
            return createGathering;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), gathering.getId()));
            data.add(gathering);
            return gathering;
        }
    }

    @Override
    public GatheringDomain getById(Long gatheringId) {
        return data.stream().filter(item -> item.getId() == gatheringId)
                .findFirst()
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public void delete(Gathering gathering) {

    }

    // 챌린지 정보와 gatheringUser 의 상태가 참여중인 정보를 필터링하여 조회한다.
    @Override
    public GatheringDomain findByIdWithGatheringUsersAndChallenge(Long gatheringId) {
        return data.stream().filter(gathering -> Objects.equals(gathering.getId(), gatheringId))
                .findFirst()
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public GatheringDomain getByIdWithGatheringUsersAndChallenge(Long gatheringId) {
        return data.stream().filter(gathering -> Objects.equals(gathering.getId(), gatheringId))
                .findFirst()
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public List<Gathering> findByIdIn(List<Long> gatheringIds) {
        return null;
    }

    @Override
    public Long findIdById(Long gatheringId) {
        return null;
    }

    @Override
    public long getActiveAndParticipatingCount(long userId) {
        return data.stream()
                .filter(gathering ->
                        gathering.getGatheringUsers().stream()
                                .anyMatch(gatheringUser ->
                                        Objects.equals(gatheringUser.getUser().getId(), userId)
                                                && Objects.equals(gatheringUser.getGatheringUserStatus(), PARTICIPATING)
                                )
                )
                .filter(gathering ->
                        !Objects.equals(gathering.getGatheringStatus(), DELETED) &&
                                !Objects.equals(gathering.getGatheringStatus(), COMPLETED)
                )
                .count();
    }

    @Override
    public long getCompletedCount(long userId) {
        return data.stream()
                .filter(gathering ->
                        gathering.getGatheringUsers().stream()
                            .anyMatch(gatheringUser -> Objects.equals(gatheringUser.getUser().getId(), userId)
                                    && Objects.equals(gatheringUser.getGatheringUserStatus(), NOT_PARTICIPATING)
                            )
                )
                .filter(gathering -> !Objects.equals(gathering.getGatheringStatus(), DELETED) &&
                        Objects.equals(gathering.getGatheringStatus(), COMPLETED)
                )
                .count();
    }

    @Override
    public long getMyCreatedCount(String userName) {
        return data.stream()
                .filter(item -> Objects.equals(item.getOwner(), userName))
                .count();
    }

    @Override
    public long getMyWishedCountByGatheringIds(Set<Long> wishGatheringIds) {
        return data.stream()
                .filter(gathering -> wishGatheringIds.contains(gathering.getId()))
                .count();
    }

    @Override
    public void join(GatheringDomain gathering) {
        data.removeIf(item -> Objects.equals(item.getId(), gathering.getId()));
        data.add(gathering);
    }

    @Override
    public void deleteById(Long gatheringId) {
        data.removeIf(item -> Objects.equals(item.getId(), gatheringId));
    }

    @Override
    public GatheringDomain findByIdWithGatheringUsers(long gatheringId) {
        return null;
    }

    @Override
    public GatheringDomain findByIdWithBookAndChallenge(Long gatheringId) {
        return data.stream()
                .filter(item -> Objects.equals(item.getId(), gatheringId))
                .findAny()
                .orElseThrow(() -> new BaseException(NON_EXISTED_GATHERING));
    }

    @Override
    public void leave(GatheringDomain gathering) {
        data.removeIf(item -> Objects.equals(item.getId(), gathering.getId()));
        data.add(gathering);
    }
}
