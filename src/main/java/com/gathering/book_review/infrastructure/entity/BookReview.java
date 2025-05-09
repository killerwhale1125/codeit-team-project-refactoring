package com.gathering.book_review.infrastructure.entity;

import com.gathering.book.infrastructure.entity.Book;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.domain.StatusType;
import com.gathering.book_review_comment.infrastructure.entity.BookReviewComment;
import com.gathering.book_review_like.infrastructure.entity.BookReviewLike;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private String title;

    @Comment("평가")
    private String apprCd;

    private String tagCd;

    private String content;

    private int likes;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @OneToMany(mappedBy = "bookReview")
    private List<BookReviewComment> bookReviewComments = new ArrayList<>();

    @OneToMany(mappedBy = "bookReview")
    private List<BookReviewLike> bookReviewLikes = new ArrayList<>();

    public static BookReview fromEntity(BookReviewDomain bookReview) {
        BookReview bookReviewEntity = new BookReview();
        bookReviewEntity.id = bookReview.getId();
        bookReviewEntity.title = bookReview.getTitle();
        bookReviewEntity.apprCd = bookReview.getApprCd();
        bookReviewEntity.tagCd = bookReview.getTagCd();
        bookReviewEntity.content = bookReview.getContent();
        bookReviewEntity.likes = bookReview.getLikes();
        bookReviewEntity.status = bookReview.getStatus();

        User user = nullableEntity(User::fromEntity, bookReview.getUser());
        if(user != null) {
            bookReviewEntity.user = user;
            user.getBookReviews().add(bookReviewEntity);
        }

        Book book = nullableEntity(Book::fromEntity, bookReview.getBook());
        if(book != null) {
            bookReviewEntity.book = book;
            book.getBookReviews().add(bookReviewEntity);
        }
        return bookReviewEntity;
    }

    public BookReviewDomain toEntity() {
        BookReviewDomain.BookReviewDomainBuilder builder = BookReviewDomain.builder()
                .id(id)
                .title(title)
                .apprCd(apprCd)
                .tagCd(tagCd)
                .content(content)
                .likes(likes)
                .status(status);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        if (book != null && getPersistenceUtil().isLoaded(book)) {
            builder.book(book.toEntity());
        }

        if (bookReviewComments != null && getPersistenceUtil().isLoaded(bookReviewComments)) {
            builder.reviewComments(bookReviewComments.stream().map(BookReviewComment::toEntity).collect(Collectors.toList()));
        }

        if (bookReviewLikes != null && getPersistenceUtil().isLoaded(bookReviewLikes)) {
            builder.reviewLikes(bookReviewLikes.stream().map(BookReviewLike::toEntity).collect(Collectors.toList()));
        }

        return builder.build();
    }
}
