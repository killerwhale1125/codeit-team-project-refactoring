package com.gathering.book_review.infrastructure;

import com.gathering.book.infrastructure.entity.Book;
import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.book_review.domain.StatusType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookReviewJpaRepository extends JpaRepository<BookReview, Long> {
    long countByUserId(long id);

    @Modifying
    @Query("UPDATE BookReview b SET b.status = :statusType WHERE b.id = :id")
    int deleteReview(@Param("id") long id, @Param("statusType") StatusType statusType);

    @Modifying
    @Query("UPDATE BookReview p SET p.likes = p.likes + 1 WHERE p.id = :reviewId")
    void incrementLikes(long reviewId);

    @Modifying
    @Query("UPDATE BookReview p SET p.likes = p.likes - 1 WHERE p.id = :reviewId")
    void decrementLikes(Long reviewId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT b FROM BookReview b WHERE b.id = :reviewId")
    BookReview findByIdWithUser(@Param("reviewId") long reviewId);

    @Query("SELECT b FROM Book b " +
            "LEFT JOIN BookReview br ON b.id = br.book.id AND br.user.id = :userId " +
            "WHERE b.id IN :bookIds AND br.id IS NULL")
    List<Book> getWroteReviewBooksCount(@Param("bookIds") List<Long> bookIds, @Param("userId") Long userId);
}
