package com.gathering.review_comment.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.domain.StatusType;
import com.gathering.review_comment.infrastructure.entity.ReviewComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ReviewCommentJpaRepository extends JpaRepository<ReviewComment, Long> {

    long countByParent(long parent);

    long countByReviewId(long id);

    default ReviewComment findByIdOrThrow(long commentId) {
        return findById(commentId).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_COMMENT));
    };
    @Modifying
    @Query("UPDATE ReviewComment b SET b.status = :statusType WHERE b.id = :id")
    int deleteComment(long id, StatusType statusType);
    @Modifying
    @Query("UPDATE ReviewComment b SET b.content = :content, b.modifiedTime = :now WHERE b.id = :id")
    int UpdateComment(@Param("id")long id, @Param("content") String content, @Param("now") LocalDateTime now);
}
