package com.gathering.review_like.infrastructure;

import com.gathering.review_like.infrastructure.entity.ReviewLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewLikesJpaRepository extends JpaRepository<ReviewLikes, Long> {
    @Query("SELECT p FROM ReviewLikes p WHERE p.review.id = :reviewId AND p.user.id = :userId")
    Optional<ReviewLikes> findByReviewIdAndUserId(long reviewId, long userId);
}
