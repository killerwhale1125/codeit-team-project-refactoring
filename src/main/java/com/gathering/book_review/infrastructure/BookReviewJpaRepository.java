package com.gathering.book_review.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.domain.StatusType;
import com.gathering.book_review.infrastructure.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookReviewJpaRepository extends JpaRepository<BookReview, Long> {

    default BookReview findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW));
    };

    long countByUserId(long id);

    @Modifying
    @Query("UPDATE BookReview b SET b.status = :statusType WHERE b.id = :id")
    int deleteReview(long id, StatusType statusType);
    @Modifying
    @Query("UPDATE BookReview p SET p.likes = p.likes + 1 WHERE p.id = :reviewId")
    void incrementLikes(long reviewId);

    @Modifying
    @Query("UPDATE BookReview p SET p.likes = p.likes - 1 WHERE p.id = :reviewId")
    void decrementLikes(long reviewId);
}
