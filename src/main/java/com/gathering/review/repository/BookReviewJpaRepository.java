package com.gathering.review.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.entitiy.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewJpaRepository extends JpaRepository<BookReview, Long> {


    default BookReview findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW));
    }
}
