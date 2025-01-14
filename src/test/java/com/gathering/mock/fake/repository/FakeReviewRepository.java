package com.gathering.mock.fake.repository;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.gathering.domain.SearchType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.constant.ReviewType;
import com.gathering.review.model.dto.*;
import com.gathering.review.repository.ReviewRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class FakeReviewRepository implements ReviewRepository {
    @Override
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, ReviewType type) {
        return null;
    }

    @Override
    public ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {
        return null;
    }

    @Override
    public ReviewListDto selectUserReviewList(String username, ReviewType type, Pageable pageable) {
        return null;
    }

    @Override
    public ReviewListDto selectBookReviewList(String username) {
        return null;
    }

    @Override
    public ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable, String username) {
        return null;
    }

    @Override
    public ReviewDto selectBookReviewDetail(long reviewId, String username) {
        return null;
    }

    @Override
    public ReviewListDto searchReviews(SearchType type, String searchParam, Pageable pageable, String username) {
        return null;
    }

    @Override
    public int DeleteReview(long reviewId, ReviewType type, String username) {
        return 0;
    }

    @Override
    public void UpdateReview(CreateReviewDto createReviewDto, long reviewId, ReviewType type, String username) {

    }

    @Override
    public void UpdateReviewLike(ReviewLikeDto reviewLikeDto, String username) {

    }

    @Override
    public int DeleteComment(long commentId, String username) {
        return 0;
    }

    @Override
    public int UpdateComment(long commentId, CreateReviewCommentDto updateReviewCommentDto, String username) {
        return 0;
    }

    @Override
    public List<BookResponse> searchUserGatheringBooks(String username) {
        return null;
    }

    @Override
    public List<BookResponse> getRecommendedKeywords() {
        return null;
    }
}
