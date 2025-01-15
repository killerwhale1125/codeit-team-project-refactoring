package com.gathering.review.service;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.gathering.domain.SearchType;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.review.domain.ReviewType;
import com.gathering.review.model.dto.*;
import com.gathering.review.service.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, ReviewType type) {
        return reviewRepository.createReview(createReviewDto, username, type);
    }

    @Override
    public ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {
        return reviewRepository.createReviewComment(createReviewCommentDto, username);
    }

    @Override
    public ReviewListDto selectUserReviewList(String username, ReviewType type, Pageable pageable) {
        return reviewRepository.selectUserReviewList(username, type, pageable);
    }

    @Override
    public ReviewListDto selectBookReviewList(String username) {
        return reviewRepository.selectBookReviewList(username);
    }

    @Override
    public ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable, String username) {
        return reviewRepository.findReviews(tag, pageable, username);
    }

    @Override
    public ReviewDto selectBookReviewDetail(long reviewId, String username) {
        return reviewRepository.selectBookReviewDetail(reviewId, username);
    }

    @Override
    public ReviewListDto searchReviews(SearchType type, String searchParam, int page, int size, String username) {
        return reviewRepository.searchReviews(type, searchParam, page, size, username);
    }

    @Override
    public int DeleteReview(long reviewId,ReviewType type, String username) {
        return reviewRepository.DeleteReview(reviewId,type, username);
    }

    @Override
    public void UpdateReview(CreateReviewDto createReviewDto, long reviewId, ReviewType type, String username) {
        reviewRepository.UpdateReview(createReviewDto, reviewId, type, username);
    }

    @Override
    public void UpdateReviewLike(ReviewLikeDto reviewLikeDto, String username) {
        reviewRepository.UpdateReviewLike(reviewLikeDto, username);
    }

    @Override
    public int DeleteComment(long commentId, String username) { return reviewRepository.DeleteComment(commentId, username); }

    @Override
    public int UpdateComment(long commentId, CreateReviewCommentDto updateReviewCommentDto, String username) {return reviewRepository.UpdateComment(commentId, updateReviewCommentDto, username);}

    @Override
    public List<BookResponse> searchUserGatheringBooks(String username) { return reviewRepository.searchUserGatheringBooks(username); }

    @Override
    public List<BookResponse> getRecommendedKeywords() { return reviewRepository.getRecommendedKeywords(); }
}
