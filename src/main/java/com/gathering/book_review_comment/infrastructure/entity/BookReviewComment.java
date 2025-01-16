package com.gathering.book_review_comment.infrastructure.entity;

import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.domain.StatusType;
import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookReviewComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id")
    @Comment("댓글 pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("작성자")
    private User user;

    @Comment("내용")
    private String content;

    @Comment("계층")
    private long parent;

    @Comment("순서")
    private int orders;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @Comment("리뷰")
    private BookReview review;

    public static BookReviewComment createEntity(BookReview review, User user
            , CreateReviewCommentDto createReviewCommentDto, int orders) {
        return BookReviewComment.builder()
                .user(user)
                .content(createReviewCommentDto.getContent())
                .parent(createReviewCommentDto.getParent())
                .orders(orders)
                .status(StatusType.Y)
                .build();
    }

    public static BookReviewComment fromEntity(BookReviewCommentDomain bookReviewCommentDomain) {
        BookReviewComment bookReviewCommentEntity = new BookReviewComment();
        bookReviewCommentEntity.id = bookReviewCommentDomain.getId();
        bookReviewCommentEntity.content = bookReviewCommentDomain.getContent();
        bookReviewCommentEntity.parent = bookReviewCommentDomain.getParent();
        bookReviewCommentEntity.status = bookReviewCommentDomain.getStatus();

        bookReviewCommentEntity.user = nullableEntity(User::fromEntity, bookReviewCommentDomain.getUser());

        BookReview bookReview = nullableEntity(BookReview::fromEntity, bookReviewCommentDomain.getReview());
        if(bookReview != null) {
            bookReviewCommentEntity.review = bookReview;
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
