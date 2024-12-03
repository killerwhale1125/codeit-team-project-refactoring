package com.gathering.gathering.repository.search;

import com.gathering.gathering.model.entity.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GatheringSearchJpaRepository extends JpaRepository<Gathering, Long>, GatheringSearchRepository {
    @Query("SELECT g FROM Gathering g JOIN FETCH g.book WHERE g.id = :gatheringId")
    Optional<Gathering> findByIdWithBooks(@Param("gatheringId") Long gatheringId);
}
