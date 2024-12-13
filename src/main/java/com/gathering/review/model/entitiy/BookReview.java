package com.gathering.review.model.entitiy;

import com.gathering.book.model.entity.Book;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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
    private String status;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> reviewComments = new ArrayList<>();

    public static BookReview createEntity(Book book, User user, CreateReviewDto createReviewDto) {
        return BookReview.builder()
                .user(user)
                .book(book)
                .title(createReviewDto.getTitle())
                .apprCd(createReviewDto.getApprCd())
                .tagCd(createReviewDto.getTag())
                .content(createReviewDto.getContent())
                .likes(0)
                .status(createReviewDto.getTmprStrgYN().equals("Y") ? "T" : "Y") // T = 임시저장, Y= '저장' , N = 삭제'
                .build();
    }
}
