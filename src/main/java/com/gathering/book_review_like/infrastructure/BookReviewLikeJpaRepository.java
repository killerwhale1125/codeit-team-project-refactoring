package com.gathering.book_review_like.infrastructure;

import com.gathering.book_review_like.infrastructure.entity.BookReviewLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookReviewLikeJpaRepository extends JpaRepository<BookReviewLike, Long> {
    @EntityGraph(attributePaths = {"bookReview"})
    @Query("SELECT p FROM BookReviewLike p WHERE p.bookReview.id = :reviewId AND p.user.id = :userId")
    Optional<BookReviewLike> findByReviewIdAndUserId(@Param("reviewId") Long reviewId, @Param("userId") Long userId);
}
