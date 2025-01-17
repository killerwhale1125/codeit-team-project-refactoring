package com.gathering.gathering_review.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.book_review.domain.StatusType;
import com.gathering.gathering_review.infrastructure.entity.GatheringReview;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface GatheringReviewJpaRepository extends JpaRepository<GatheringReview, Long> {

    default GatheringReview findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW));
    };

    @Modifying
    @Query("UPDATE GatheringReview b SET b.status = :statusType WHERE b.id = :id")
    int deleteReview(long id, StatusType statusType);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT b FROM GatheringReview b WHERE b.id = :reviewId")
    GatheringReview findByIdWithUser(@Param("reviewId") long reviewId);

    @Modifying
    @Query("UPDATE GatheringReview b SET b.content = :content, b.score = :score, b.modifiedTime = :modifiedTime WHERE b.id = :id")
    void update(@Param("id") Long id,
                @Param("content") String content,
                @Param("score") int score,
                @Param("modifiedTime") LocalDateTime modifiedTime);
}
