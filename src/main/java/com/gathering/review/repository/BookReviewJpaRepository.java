package com.gathering.review.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review.model.entitiy.BookReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookReviewJpaRepository extends JpaRepository<BookReview, Long> {

    default BookReview findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW));
    };

    long countByUserId(long id);
    @Modifying
    @Query("UPDATE BookReview b SET b.status = :statusType WHERE b.id = :id")
    int deleteReview(long id, StatusType statusType);

}
