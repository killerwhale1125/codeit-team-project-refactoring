package com.gathering.gathering_review.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.domain.StatusType;
import com.gathering.gathering_review.infrastructure.entity.GatheringReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GatheringReviewJpaRepository extends JpaRepository<GatheringReview, Long> {

    default GatheringReview findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW));
    };
    @Modifying
    @Query("UPDATE GatheringReview b SET b.status = :statusType WHERE b.id = :id")
    int deleteReview(long id, StatusType statusType);

}
