package com.gathering.book.repository;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.book.model.entity.Book;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN b.bookCategories bc WHERE b.id = :bookId AND bc.category.id = :categoryId")
    Optional<Book> findBookByBookIdAndCategoryId(@Param("bookId") Long bookId, @Param("categoryId") Long categoryId);

    boolean existsByTitle(String title);

    /**
     * 책 Title에 FULL TEXT INDEX 적용
     */
    @Query(value = "SELECT * FROM BOOK b WHERE MATCH(b.title) AGAINST(:searchWord IN BOOLEAN MODE)", nativeQuery = true)
    List<Book> searchBooksBySearchWord(@Param("searchWord") String searchWord);


    default Book findByIdOrThrow(long id) {
        return  findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.BOOK_OR_CATEGORY_NOT_FOUND));
    }

    @Modifying
    @Query("UPDATE Book b SET b.selectedCount = :selectedCount WHERE b.id = :bookId")
    void updateSelectedCount(@Param("bookId") Long bookId, @Param("selectedCount") long selectedCount);
}
