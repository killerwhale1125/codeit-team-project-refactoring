package com.gathering.review.service.port;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.gathering.domain.SearchType;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.review.domain.ReviewType;
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

    ReviewListDto searchReviews(SearchType type, String searchParam, int page, int size, String username);

    int DeleteReview(long reviewId,ReviewType type, String username);

    void UpdateReview(CreateReviewDto createReviewDto, long reviewId, ReviewType type, String username);

    void UpdateReviewLike(ReviewLikeDto reviewLikeDto, String username);

    int DeleteComment(long commentId, String username);

    int UpdateComment(long commentId, CreateReviewCommentDto updateReviewCommentDto, String username);

    List<BookResponse> searchUserGatheringBooks(String username);

    List<BookResponse> getRecommendedKeywords();
}
