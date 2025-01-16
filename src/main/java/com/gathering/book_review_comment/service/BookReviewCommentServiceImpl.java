package com.gathering.book_review_comment.service;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.service.port.BookReviewRepository;
import com.gathering.book_review_comment.controller.port.BookReviewCommentService;
import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import com.gathering.book_review_comment.domain.BookReviewCommentCreate;
import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.book_review_comment.service.port.BookReviewCommentRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewCommentServiceImpl implements BookReviewCommentService {

    private final BookReviewCommentRepository bookReviewCommentRepository;
    private final BookReviewRepository bookReviewRepository;
    private final UserRepository userRepository;

    @Override
    public BookReviewCommentResponse create(String username, BookReviewCommentCreate bookReviewCommentCreate) {
        UserDomain user = userRepository.findByUsername(username);
        BookReviewDomain bookReview = bookReviewRepository.findById(bookReviewCommentCreate.getReviewId());

        BookReviewCommentDomain bookReviewComment = bookReviewCommentRepository.save(BookReviewCommentDomain.create(bookReviewCommentCreate, user, bookReview));
        return BookReviewCommentResponse.fromEntity(bookReviewComment);
    }
}
