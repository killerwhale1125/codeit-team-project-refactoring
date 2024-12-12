package com.gathering.review.repository;

import com.gathering.review.model.entitiy.GatheringReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringReviewJpaRepository extends JpaRepository<GatheringReview, Long> {

}
