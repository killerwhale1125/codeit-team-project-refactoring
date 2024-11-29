package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringJpaRepository extends JpaRepository<Gathering, Long> {
}
