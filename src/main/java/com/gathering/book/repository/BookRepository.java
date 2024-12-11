package com.gathering.book.repository;

import com.gathering.book.model.entity.Book;

public interface BookRepository {
    Book findBookByBookIdAndCategoryId(Long bookId, Long categoryId);

    boolean existsByTitle(String title);

    void save(Book book);

    Book findById(Long bookId);
}
