package com.gathering.gathering.repository.search;

import com.gathering.gathering.model.entity.Gathering;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GatheringSearchJpaRepository extends JpaRepository<Gathering, Long>, GatheringSearchRepository {

    @EntityGraph(attributePaths = {"challenge", "book", "image"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> getGatheringWithChallengeAndBook(@Param("gatheringId") Long gatheringId);

    @Query(value = "SELECT g.GATHERING_ID, g.NAME, g.THUMBNAIL, g.GOAL_DAYS, g.MAX_CAPACITY, g.CURRENT_CAPACITY, " +
            "c.READING_TIME_GOAL, b.TITLE, b.IMAGE " +
            "FROM GATHERING g " +
            "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
            "LEFT JOIN CHALLENGE c ON g.CHALLENGE_ID = c.CHALLENGE_ID " +
            "WHERE MATCH(g.name, b.title) AGAINST(:searchWord IN BOOLEAN MODE) > 0",
            countQuery = "SELECT COUNT(DISTINCT g.GATHERING_ID) FROM GATHERING g " +
                    "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
                    "WHERE MATCH(g.name, b.title) AGAINST(:searchWord IN BOOLEAN MODE) > 0",
            nativeQuery = true)
    Page<Tuple> findGatheringsBySearchWord(@Param("searchWord") String searchWord, Pageable pageable);

}
