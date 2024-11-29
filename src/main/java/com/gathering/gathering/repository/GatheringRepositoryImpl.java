package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepository {

    private final GatheringJpaRepository gatheringJpaRepository;

    @Override
    public void save(Gathering gathering) {
        gatheringJpaRepository.save(gathering);
    }
}
