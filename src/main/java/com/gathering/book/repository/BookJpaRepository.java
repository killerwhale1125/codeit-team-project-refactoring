package com.gathering.book.repository;

import com.gathering.book.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN BookCategory bc ON b.id = bc.book.id WHERE b.id = :bookId AND bc.category.id = :categoryId")
    Optional<Book> findBookByBookIdAndCategoryId(@Param("bookId") Long bookId, @Param("categoryId") Long categoryId);

    boolean existsByTitle(String title);
}
