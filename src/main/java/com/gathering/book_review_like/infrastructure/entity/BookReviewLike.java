package com.gathering.book_review_like.infrastructure.entity;

import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.book_review_like.domain.BookReviewLikeDomain;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Table(name = "book_review_like",
        uniqueConstraints={
                @UniqueConstraint(
                        name="book_review_like_uk",
                        columnNames={"user_id", "book_review_id"}
                )
        })
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_review_like_id")
    @Comment("좋아요 pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("사용자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_review_id")
    @Comment("리뷰 아이디")
    private BookReview bookReview;

    public static BookReviewLike fromEntity(BookReviewLikeDomain bookReviewLike) {
        BookReviewLike bookReviewLikeEntity = new BookReviewLike();
        bookReviewLikeEntity.id = bookReviewLike.getId();
        bookReviewLikeEntity.user = nullableEntity(User::fromEntity, bookReviewLike.getUser());

        BookReview bookReview = nullableEntity(BookReview::fromEntity, bookReviewLike.getBookReview());
        if(bookReview != null) {
            bookReviewLikeEntity.bookReview = bookReview;
            bookReview.getBookReviewLikes().add(bookReviewLikeEntity);
        }

        return bookReviewLikeEntity;
    }

    public BookReviewLikeDomain toEntity() {
        BookReviewLikeDomain.BookReviewLikeDomainBuilder builder = BookReviewLikeDomain.builder()
                .id(id);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        return builder.build();
    }
}
