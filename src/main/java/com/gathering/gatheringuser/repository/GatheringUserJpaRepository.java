package com.gathering.gatheringuser.repository;

import com.gathering.gatheringuser.model.entity.GatheringUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringUserJpaRepository extends JpaRepository<GatheringUser, Long> {
}
