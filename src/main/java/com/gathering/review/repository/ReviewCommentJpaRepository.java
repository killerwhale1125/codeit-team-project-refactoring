package com.gathering.review.repository;

import com.gathering.review.model.entitiy.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentJpaRepository extends JpaRepository<ReviewComment, Long> {

    long countByParent(long parent);

    long countByReviewId(long id);
}
