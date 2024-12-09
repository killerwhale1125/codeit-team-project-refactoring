package com.gathering.review.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.entitiy.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {


    default Review findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW));
    }
}
