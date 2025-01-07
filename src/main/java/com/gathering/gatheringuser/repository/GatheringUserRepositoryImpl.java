package com.gathering.gatheringuser.repository;

import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GatheringUserRepositoryImpl implements GatheringUserRepository {
    private final GatheringUserJpaRepository gatheringUserJpaRepository;

    @Override
    public GatheringUserDomain save(GatheringUserDomain gatheringUserDomain) {
        return gatheringUserJpaRepository.save(GatheringUser.fromEntity(gatheringUserDomain)).toEntity();
    }
}
