package com.gathering.book_review.service;

import com.gathering.book.domain.BookDomain;
import com.gathering.book.service.port.BookRepository;
import com.gathering.book_review.controller.port.BookReviewService;
import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.domain.BookReviewCreate;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.service.port.BookReviewRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;

    @Override
    public BookReviewResponse create(String username, BookReviewCreate bookReviewCreate) {
        UserDomain user = userRepository.findByUsername(username);
        BookDomain book = bookRepository.findById(bookReviewCreate.getBookId());
        BookReviewDomain bookReview = bookReviewRepository.save(BookReviewDomain.create(bookReviewCreate, book, user));

        return BookReviewResponse.fromEntity(bookReview);
    }
}
