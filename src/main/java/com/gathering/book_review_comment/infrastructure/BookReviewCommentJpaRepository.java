package com.gathering.book_review_comment.infrastructure;

import com.gathering.book_review_comment.infrastructure.entity.BookReviewComment;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.book_review.domain.StatusType;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookReviewCommentJpaRepository extends JpaRepository<BookReviewComment, Long> {

    @Modifying
    @Query("UPDATE BookReviewComment b SET b.status = :statusType WHERE b.id = :commentId")
    int deleteComment(@Param("commentId") Long commentId, @Param("statusType") StatusType statusType);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT b FROM BookReviewComment b WHERE b.id = :commentId")
    BookReviewComment findByIdWithUser(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE BookReviewComment b SET b.content = :content WHERE b.id = :commentId")
    void update(@Param("commentId") Long commentId, String content);

}
