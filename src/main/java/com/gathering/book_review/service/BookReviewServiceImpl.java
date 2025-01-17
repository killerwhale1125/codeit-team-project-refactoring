package com.gathering.book_review.service;

import com.gathering.book.domain.BookDomain;
import com.gathering.book.service.port.BookRepository;
import com.gathering.book_review.controller.port.BookReviewService;
import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.domain.BookReviewCreate;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.domain.BookReviewUpdate;
import com.gathering.book_review.infrastructure.BookReviewSearchRepository;
import com.gathering.book_review.service.port.BookReviewRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewSearchRepository bookReviewSearchRepository;

    @Override
    @Transactional
    public BookReviewResponse create(String username, BookReviewCreate bookReviewCreate) {
        UserDomain user = userRepository.findByUsername(username);
        BookDomain book = bookRepository.findById(bookReviewCreate.getBookId());
        BookReviewDomain bookReview = bookReviewRepository.save(BookReviewDomain.create(bookReviewCreate, book, user));

        return BookReviewResponse.fromEntity(bookReview);
    }

    @Override
    public void delete(long reviewId, String username) {
        BookReviewDomain bookReview = bookReviewSearchRepository.findByIdWithUser(reviewId);
        bookReview = bookReview.delete(username);
        bookReviewRepository.delete(bookReview);
    }

    @Override
    public BookReviewDomain update(BookReviewUpdate bookReviewUpdate, Long reviewId, String username) {
        UserDomain user = userRepository.findByUsername(username);
        BookReviewDomain bookReview = bookReviewSearchRepository.findByIdWithUser(reviewId);
        BookDomain book = bookRepository.findById(bookReviewUpdate.getBookId());
        bookReview = bookReview.update(bookReviewUpdate, user, book);
        return bookReviewRepository.save(bookReview);
    }
}
