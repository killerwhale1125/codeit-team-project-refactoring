package com.gathering.book_review.controller.port;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book_review.controller.response.BookReviewDetailsResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.gathering.domain.SearchType;

import java.util.List;

public interface BookReviewSearchService {
    BookReviewsResponse myReviews(String username, int page, int size);

    BookReviewsResponse bookReviews(String username);

    BookReviewsResponse reviewsByTag(String username, BookReviewTagType tag, int page, int size);

    BookReviewDetailsResponse detail(long reviewId, String usernameOrElseGet);

    BookReviewsResponse searchByWord(SearchType type, String searchWord, String usernameOrElseGet, int page, int size);

    List<BookResponse> unReviewedBooks(String username);

}
