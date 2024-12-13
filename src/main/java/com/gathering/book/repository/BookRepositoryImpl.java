package com.gathering.book.repository;

import com.gathering.book.model.dto.BookSearchResponse;
import com.gathering.book.model.dto.QBookSearchResponse;
import com.gathering.book.model.entity.Book;
import com.gathering.common.base.exception.BaseException;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gathering.book.model.entity.QBook.book;
import static com.gathering.common.base.response.BaseResponseStatus.BOOK_OR_CATEGORY_NOT_FOUND;
import static com.gathering.common.base.response.BaseResponseStatus.NOT_EXISTED_BOOK;
import static com.gathering.gathering.model.entity.QGathering.gathering;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final BookJpaRepository bookJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Book findBookByBookIdAndCategoryId(Long bookId, Long categoryId) {
        return bookJpaRepository.findBookByBookIdAndCategoryId(bookId, categoryId)
                .orElseThrow(() -> new BaseException(BOOK_OR_CATEGORY_NOT_FOUND));
    }

    @Override
    public boolean existsByTitle(String title) {
        return bookJpaRepository.existsByTitle(title);
    }

    @Override
    public void save(Book book) {
        bookJpaRepository.save(book);
    }

    @Override
    public Book findById(Long bookId) {
        return bookJpaRepository.findById(bookId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOOK));
    }

    @Override
    public List<BookSearchResponse> findPopularBooks(Pageable pageable) {
        return queryFactory.select(new QBookSearchResponse(book.id, book.title, book.image, // 서브쿼리로 모임 수 카운트
                        JPAExpressions
                                .select(gathering.count())
                                .from(gathering)
                                .where(gathering.book.id.eq(book.id))
                ))
                .from(book)
                .orderBy(book.selectedCount.desc())
                .offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();
    }
}
