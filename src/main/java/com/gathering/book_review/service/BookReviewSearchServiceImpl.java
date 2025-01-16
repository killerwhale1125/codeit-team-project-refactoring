package com.gathering.book_review.service;

import com.gathering.book_review.controller.port.BookReviewSearchService;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.infrastructure.BookReviewSearchRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewSearchServiceImpl implements BookReviewSearchService {

    private final BookReviewSearchRepository bookReviewSearchRepository;
    private final UserRepository userRepository;

    @Override
    public BookReviewsResponse myReviews(String username, int page, int size) {
        UserDomain user = userRepository.findByUsername(username);
        return bookReviewSearchRepository.getMyReviews(user, page, size);
    }
}
