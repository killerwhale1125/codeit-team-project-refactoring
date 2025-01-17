package com.gathering.book_review.infrastructure;

import com.gathering.book.domain.BookDomain;
import com.gathering.book_review.controller.response.BookReviewDetailsResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.gathering.domain.SearchType;
import com.gathering.user.domain.UserDomain;

import java.util.List;

public interface BookReviewSearchRepository {
    BookReviewsResponse getMyReviews(UserDomain user, int page, int size);

    BookReviewsResponse getBookReviews(String username);

    BookReviewsResponse findReviewsByTag(String username, BookReviewTagType tag, int page, int size);

    BookReviewDetailsResponse getDetailBy(long reviewId, String username);

    BookReviewsResponse searchByWord(SearchType type, String searchWord, String username, int page, int size);

    BookReviewDomain findByIdWithUser(Long reviewId);

    List<BookDomain> getWroteReviewBooksCount(List<Long> bookIds, Long userId);
}
