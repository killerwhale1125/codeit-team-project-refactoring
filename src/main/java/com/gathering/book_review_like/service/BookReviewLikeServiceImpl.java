package com.gathering.book_review_like.service;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.service.port.BookReviewRepository;
import com.gathering.book_review_like.controller.port.BookReviewLikeService;
import com.gathering.book_review_like.domain.BookReviewLikeCreate;
import com.gathering.book_review_like.domain.BookReviewLikeDomain;
import com.gathering.book_review_like.service.port.BookReviewLikeRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewLikeServiceImpl implements BookReviewLikeService {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final UserRepository userRepository;
    private final BookReviewRepository bookReviewRepository;

    @Override
    public void like(BookReviewLikeCreate bookReviewLikeCreate, String username) {
        UserDomain user = userRepository.findByUsername(username);

        BookReviewLikeDomain bookReviewLike = bookReviewLikeRepository.findByReviewIdAndUserIdOrElse(bookReviewLikeCreate.getReviewId(), user.getId());
        BookReviewDomain bookReview = bookReviewRepository.findById(bookReviewLike.getId());
        if(bookReviewLike != null) {
            bookReviewLikeRepository.deleteById(bookReviewLike.getId());
            bookReviewRepository.decrementLike(bookReview.getId());
        } else {
            bookReviewLikeRepository.save(BookReviewLikeDomain.create(bookReview, user));
            bookReviewRepository.incrementLike(bookReview.getId());
        }
    }
}
