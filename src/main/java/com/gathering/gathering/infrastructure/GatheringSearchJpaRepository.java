package com.gathering.gathering.infrastructure;

import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.infrastructure.entity.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GatheringSearchJpaRepository extends JpaRepository<Gathering, Long> {

    @EntityGraph(attributePaths = {"challenge", "book", "image"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> getByIdWithChallengeAndBook(@Param("gatheringId") Long gatheringId);

    @Query(value = "SELECT " +
            "g.GATHERING_ID, g.NAME, g.CURRENT_CAPACITY, g.MAX_CAPACITY, g.GATHERING_WEEK, g.GATHERING_STATUS, g.START_DATE, c.READING_TIME_GOAL, i.IMAGE_URL, b.BOOK_ID, b.TITLE, b.IMAGE " +
            "FROM GATHERING g " +
            "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
            "LEFT JOIN IMAGE i ON g.IMAGE_ID = i.IMAGE_ID " +
            "LEFT JOIN CHALLENGE c ON g.CHALLENGE_ID = c.CHALLENGE_ID " +
            "WHERE MATCH(g.name) AGAINST (:searchWord IN BOOLEAN MODE)",
            nativeQuery = true,
            countQuery = "SELECT " +
                    "COUNT(g.GATHERING_ID) " +
                    "FROM GATHERING g " +
                    "WHERE MATCH(g.name) AGAINST (:searchWord IN BOOLEAN MODE)")
    Page<Object[]> findGatheringsBySearchWordAndTypeTitle(@Param("searchWord") String searchWord, Pageable pageable);

    @Query(value = "SELECT " +
            "g.GATHERING_ID, g.NAME, g.CURRENT_CAPACITY, g.MAX_CAPACITY, g.GATHERING_WEEK, g.GATHERING_STATUS, g.START_DATE, c.READING_TIME_GOAL, i.IMAGE_URL, b.BOOK_ID, b.TITLE, b.IMAGE " +
            "FROM GATHERING g " +
            "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
            "LEFT JOIN IMAGE i ON g.IMAGE_ID = i.IMAGE_ID " +
            "LEFT JOIN CHALLENGE c ON g.CHALLENGE_ID = c.CHALLENGE_ID " +
            "WHERE MATCH(b.introduce) AGAINST (?1 IN BOOLEAN MODE)",
            nativeQuery = true,
            countQuery = "SELECT " +
                    "COUNT(g.GATHERING_ID) " +
                    "FROM GATHERING g " +
                    "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
                    "WHERE MATCH(b.introduce) AGAINST (:searchWord IN BOOLEAN MODE)")
    Page<Object[]> findGatheringsBySearchWordAndTypeContent(@Param("searchWord") String searchWord, Pageable pageable);

    @Query(value = "SELECT " +
            "g.GATHERING_ID, g.NAME, g.CURRENT_CAPACITY, g.MAX_CAPACITY, g.GATHERING_WEEK, g.GATHERING_STATUS, g.START_DATE, c.READING_TIME_GOAL, i.IMAGE_URL, b.BOOK_ID, b.TITLE, b.IMAGE " +
            "FROM GATHERING g " +
            "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
            "LEFT JOIN IMAGE i ON g.IMAGE_ID = i.IMAGE_ID " +
            "LEFT JOIN CHALLENGE c ON g.CHALLENGE_ID = c.CHALLENGE_ID " +
            "WHERE MATCH(b.title) AGAINST (?1 IN BOOLEAN MODE)",
            nativeQuery = true,
            countQuery = "SELECT " +
                    "COUNT(g.GATHERING_ID) " +
                    "FROM GATHERING g " +
                    "LEFT JOIN BOOK b ON g.BOOK_ID = b.BOOK_ID " +
                    "WHERE MATCH(b.title) AGAINST (:searchWord IN BOOLEAN MODE)")
    Page<Object[]> findGatheringsBySearchWordAndTypeBookName(@Param("searchWord") String searchWord, Pageable pageable);

    @Query("SELECT g.book.id FROM Gathering g JOIN g.gatheringUsers gu WHERE gu.user.id = :userId AND g.gatheringStatus = :gatheringStatus")
    List<Long> findCompletedGatheringBookIdsByUserId(@Param("userId") Long userId, @Param("gatheringStatus") GatheringStatus gatheringStatus);
}
