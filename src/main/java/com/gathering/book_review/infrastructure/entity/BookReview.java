package com.gathering.book_review.infrastructure.entity;

import com.gathering.book.infrastructure.entity.Book;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review_comment.infrastructure.entity.BookReviewComment;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.domain.StatusType;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review_like.infrastructure.entity.ReviewLikes;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    @Comment("리뷰 pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("작성자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @Comment("책")
    private Book book;

    @Comment("제목")
    private String title;

    @Comment("평가")
    private String apprCd;

    @Comment("태그")
    private String tagCd;

    @Comment("내용")
    private String content;

    @Comment("좋아요")
    private int likes;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @OneToMany(mappedBy = "review")
    private List<BookReviewComment> bookReviewComments = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    private List<ReviewLikes> reviewLikes;

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


    public void updateReview(CreateReviewDto dto, Book book) {
        this.book = book;
        this.title = dto.getTitle();
        this.apprCd = dto.getApprCd();
        this.tagCd = dto.getTag();
        this.content = dto.getContent();
        this.modifiedTime = LocalDateTime.now();
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

        if (reviewLikes != null && getPersistenceUtil().isLoaded(reviewLikes)) {
            builder.reviewLikes(reviewLikes.stream().map(ReviewLikes::toEntity).collect(Collectors.toList()));
        }

        return builder.build();
    }
}
