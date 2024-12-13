package com.gathering.book.repository;

import com.gathering.book.model.dto.BookSearchResponse;
import com.gathering.book.model.entity.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepository {
    Book findBookByBookIdAndCategoryId(Long bookId, Long categoryId);

    boolean existsByTitle(String title);

    void save(Book book);

    Book findById(Long bookId);

    List<BookSearchResponse> findPopularBooks(Pageable pageable);
}
