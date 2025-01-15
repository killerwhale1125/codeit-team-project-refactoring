package com.gathering.book_review.infrastructure.entity;

import com.gathering.book.infrastructure.entity.Book;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.domain.StatusType;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review_comment.infrastructure.entity.ReviewComment;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    @Comment("리뷰 pk")
    private long id;

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

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> reviewComments = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewLikes> reviewLikes;


    public void updateReview(CreateReviewDto dto, Book book) {
        this.book = book;
        this.title = dto.getTitle();
        this.apprCd = dto.getApprCd();
        this.tagCd = dto.getTag();
        this.content = dto.getContent();
        this.modifiedTime = LocalDateTime.now();
    }

    public static BookReview createEntity(Book book, User user, CreateReviewDto createReviewDto) {
        return null;
//        return BookReview.builder()
//                .user(user)
//                .book(book)
//                .title(createReviewDto.getTitle())
//                .apprCd(createReviewDto.getApprCd())
//                .tagCd(createReviewDto.getTag())
//                .content(createReviewDto.getContent())
//                .likes(0)
//                .status(createReviewDto.getTmprStrgYN().equals("Y") ? StatusType.T : StatusType.Y) // T = 임시저장, Y= '저장' , N = 삭제'
//                .build();
    }
}
