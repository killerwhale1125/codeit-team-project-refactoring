package com.gathering.review.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.constant.ReviewType;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.ReviewLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewLikesJpaRepository extends JpaRepository<ReviewLikes, Long> {
    @Query("SELECT p FROM ReviewLikes p WHERE p.review.id = :reviewId AND p.user.id = :userId")
    Optional<ReviewLikes> findByReviewIdAndUserId(long reviewId, long userId);
}
