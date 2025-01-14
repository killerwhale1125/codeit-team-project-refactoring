package com.gathering.mock.fake.repository;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.gatheringuser.service.port.GatheringUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FakeGatheringUserRepository implements GatheringUserRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private List<GatheringUserDomain> data = new ArrayList<>();

    @Override
    public GatheringUserDomain save(GatheringUserDomain gatheringUser) {
        if(Objects.isNull(gatheringUser.getId())) {
            GatheringDomain gathering = gatheringUser.getGathering();
            GatheringUserDomain createGatheringUser = GatheringUserDomain.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .gatheringUserStatus(gatheringUser.getGatheringUserStatus())
                    .user(gatheringUser.getUser())
                    .gathering(gathering)
                    .build();
            gathering.getGatheringUsers().add(createGatheringUser); // 양방향 관계 설정
            data.add(createGatheringUser);
            return createGatheringUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), gatheringUser.getId()));
            data.add(gatheringUser);
            return gatheringUser;
        }
    }

    @Override
    public void deleteById(Long gatheringUserId) {
        data.removeIf(item -> Objects.equals(item.getId(), gatheringUserId));
    }

    @Override
    public void join(GatheringUserDomain gatheringUserDomain) {

    }

    @Override
    public List<GatheringUserDomain> findByGatheringIdAndStatusWithUsers(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        return data.stream()
                .filter(item ->
                        item.getId() == gatheringId && item.getGatheringUserStatus().equals(gatheringUserStatus))
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<GatheringUserDomain> gatheringUsers) {
        gatheringUsers.stream().forEach(gatheringUser -> {
            if(Objects.isNull(gatheringUser.getId())) {
                GatheringDomain gathering = gatheringUser.getGathering();
                GatheringUserDomain createGatheringUser = GatheringUserDomain.builder()
                        .id(autoGeneratedId.incrementAndGet())
                        .gatheringUserStatus(gatheringUser.getGatheringUserStatus())
                        .user(gatheringUser.getUser())
                        .gathering(gathering)
                        .build();
                gathering.getGatheringUsers().add(createGatheringUser); // 양방향 관계 설정
                data.add(createGatheringUser);
            } else {
                data.removeIf(item -> Objects.equals(item.getId(), gatheringUser.getId()));
                data.add(gatheringUser);
            }
        });
    }
}
