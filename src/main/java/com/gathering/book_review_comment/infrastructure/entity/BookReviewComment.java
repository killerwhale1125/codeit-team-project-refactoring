package com.gathering.book_review_comment.infrastructure.entity;

import com.gathering.book_review.domain.StatusType;
import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReviewComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_review_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_review_id")
    private BookReview bookReview;

    private String content;
    private long parent;
    private int orders;

    public static BookReviewComment fromEntity(BookReviewCommentDomain bookReviewCommentDomain) {
        BookReviewComment bookReviewCommentEntity = new BookReviewComment();
        bookReviewCommentEntity.id = bookReviewCommentDomain.getId();
        bookReviewCommentEntity.content = bookReviewCommentDomain.getContent();
        bookReviewCommentEntity.parent = bookReviewCommentDomain.getParent();
        bookReviewCommentEntity.status = bookReviewCommentDomain.getStatus();

        bookReviewCommentEntity.user = nullableEntity(User::fromEntity, bookReviewCommentDomain.getUser());

        BookReview bookReview = nullableEntity(BookReview::fromEntity, bookReviewCommentDomain.getReview());
        if(bookReview != null) {
            bookReviewCommentEntity.bookReview = bookReview;
            bookReview.getBookReviewComments().add(bookReviewCommentEntity);
        }

        return bookReviewCommentEntity;
    }

    // 재귀
    public BookReviewCommentDomain toEntity() {
        BookReviewCommentDomain.BookReviewCommentDomainBuilder builder = BookReviewCommentDomain.builder()
                .content(content)
                .parent(parent)
                .orders(orders)
                .status(status)
                .createdTime(createdTime)
                .modifiedTime(modifiedTime);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        return builder.build();
    }
}
