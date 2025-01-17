package com.gathering.book.service.port;

import com.gathering.book.controller.response.BookSearchResponse;
import com.gathering.book.domain.BookDomain;
import com.gathering.book.infrastructure.entity.Book;

import java.util.List;

public interface BookRepository {

    boolean existsByTitle(String title);

    BookDomain save(BookDomain book);

    BookDomain findById(Long bookId);

    List<BookSearchResponse> findPopularBooks(int page, int size);

    List<BookDomain> searchBooksBySearchWord(String title);

    void updateSelectedCount(BookDomain book);
}
