package com.gathering.gatheringuser.repository;

import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GatheringUserRepositoryImpl implements GatheringUserRepository {
    private final GatheringUserJpaRepository gatheringUserJpaRepository;

    @Override
    public GatheringUserDomain save(GatheringUserDomain gatheringUserDomain) {
        return gatheringUserJpaRepository.save(GatheringUser.fromEntity(gatheringUserDomain)).toEntity();
    }

    @Override
    public void deleteById(Long gatheringUserId) {
        gatheringUserJpaRepository.deleteById(gatheringUserId);
    }

    @Override
    public void join(GatheringUserDomain gatheringUserDomain) {
        gatheringUserJpaRepository.save(GatheringUser.fromEntity(gatheringUserDomain));
    }

    @Override
    public List<GatheringUserDomain> findByGatheringIdAndStatusWithUsers(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        return gatheringUserJpaRepository.findByGatheringIdWithUsers(gatheringId, gatheringUserStatus).stream()
                .map(GatheringUser::toEntity)
                .collect(Collectors.toList());
    }
}
