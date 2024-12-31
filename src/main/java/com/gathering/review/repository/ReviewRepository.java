package com.gathering.review.repository;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.constant.ReviewType;
import com.gathering.review.model.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {
    ReviewDto createReview(CreateReviewDto createReviewDto, String username, ReviewType type);

    ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username);

    ReviewListDto selectUserReviewList(String username, ReviewType type, Pageable pageable);

    ReviewListDto selectBookReviewList(String username);

    ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable, String username);

    ReviewDto selectBookReviewDetail(long reviewId, String username);

    ReviewListDto searchReviews(SearchType type, String searchParam, Pageable pageable, String username);

    int DeleteReview(long reviewId,ReviewType type, String username);

    void UpdateReview(CreateReviewDto createReviewDto, long reviewId, ReviewType type, String username);

    void UpdateReviewLike(ReviewLikeDto reviewLikeDto, String username);

    int DeleteComment(long commentId, String username);

    int UpdateComment(long commentId, CreateReviewCommentDto updateReviewCommentDto, String username);

    List<BookResponse> searchUserGatheringBooks(String username);

    List<BookResponse> getRecommendedKeywords();
}
