package com.gathering.book.service.port;

import com.gathering.book.controller.response.BookSearchResponse;
import com.gathering.book.domain.BookDomain;
import com.gathering.book.infrastructure.entity.Book;

import java.util.List;

public interface BookRepository {
    Book findBookByBookIdAndCategoryId(Long bookId, Long categoryId);

    boolean existsByTitle(String title);

    BookDomain save(BookDomain book);

    Book save(Book book);

    BookDomain findById(Long bookId);

    List<BookSearchResponse> findPopularBooks(int page, int size);

    List<Book> searchBooksBySearchWord(String title);

    void updateSelectedCount(BookDomain book);
}
