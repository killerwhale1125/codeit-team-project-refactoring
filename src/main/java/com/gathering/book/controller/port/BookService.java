package com.gathering.book.controller.port;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book.controller.response.BookSearchResponse;

import java.util.List;

public interface BookService {
    List<BookSearchResponse> findPopularBooks(int page, int size);

    List<BookResponse> searchBooksBySearchWord(String title);
}
