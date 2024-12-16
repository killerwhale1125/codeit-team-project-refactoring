package com.gathering.review.repository;

import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.dto.*;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository {
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type);

    ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);

    ReviewListDto selectUserReviewList(String username, String type);

    ReviewListDto selectBookReviewList(String username);

    ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable);

    ReviewDto selectBookReviewDetail(long reviewId);
}
