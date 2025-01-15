package com.gathering.book.infrastructure;

import com.gathering.book.domain.BookDomain;
import com.gathering.book.controller.response.BookSearchResponse;
import com.gathering.book.model.dto.QBookSearchResponse;
import com.gathering.book.infrastructure.entity.Book;
import com.gathering.book.service.port.BookRepository;
import com.gathering.common.base.exception.BaseException;
import com.gathering.util.string.FullTextIndexParser;
import com.gathering.util.string.StringUtil;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gathering.book.model.entity.QBook.book;
import static com.gathering.common.base.response.BaseResponseStatus.*;
import static com.gathering.gathering.infrastructure.entity.QGathering.gathering;

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
    public BookDomain save(BookDomain book) {
        return bookJpaRepository.save(Book.fromEntity(book)).toEntity();
    }

    public Book save(Book book) {
        return bookJpaRepository.save(book);
    }

    @Override
    public BookDomain findById(Long bookId) {
        return bookJpaRepository.findById(bookId).orElseThrow(() -> new BaseException(NOT_EXISTED_BOOK)).toEntity();
    }

    @Override
    public List<BookSearchResponse> findPopularBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
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

    @Override
    public List<Book> searchBooksBySearchWord(String searchWord) {
        if (!StringUtil.isValidLength(searchWord, 3)) {
            throw new BaseException(INVALID_SEARCH_WORD);
        }
        return bookJpaRepository.searchBooksBySearchWord(FullTextIndexParser.formatForFullTextQuery(searchWord));
    }

    @Override
    public void updateSelectedCount(BookDomain book) {
        bookJpaRepository.updateSelectedCount(book.getId(), book.getSelectedCount());
    }
}
