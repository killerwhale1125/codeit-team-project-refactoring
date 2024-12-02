package com.gathering.book.repository;

import com.gathering.book.model.entity.Book;
import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gathering.common.base.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final BookJpaRepository bookJpaRepository;

    @Override
    public Book findBookByBookIdAndCategoryId(Long bookId, Long categoryId) {
        return bookJpaRepository.findBookByBookIdAndCategoryId(bookId, categoryId)
                .orElseThrow(() -> new BaseException(BOOK_OR_CATEGORY_NOT_FOUND));
    }
}
