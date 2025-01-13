package com.gathering.book.repository;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.book.model.dto.BookResponse;
import com.gathering.book.model.dto.BookSearchResponse;
import com.gathering.book.model.entity.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepository {
    Book findBookByBookIdAndCategoryId(Long bookId, Long categoryId);

    boolean existsByTitle(String title);

    BookDomain save(BookDomain book);

    Book save(Book book);

    BookDomain findById(Long bookId);

    List<BookSearchResponse> findPopularBooks(Pageable pageable);

    List<Book> searchBooksBySearchWord(String title);

    void updateSelectedCount(BookDomain book);
}
